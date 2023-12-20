package com.techpower.airbnb.service.impl;

import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.techpower.airbnb.constant.Order;
import com.techpower.airbnb.converter.FeedbackConverter;
import com.techpower.airbnb.converter.RoomConverter;
import com.techpower.airbnb.dto.FeedbackDTO;
import com.techpower.airbnb.dto.RoomDTO;
import com.techpower.airbnb.entity.*;
import com.techpower.airbnb.repository.*;
import com.techpower.airbnb.request.SearchHouseRequest;
import com.techpower.airbnb.response.DayBooking;
import com.techpower.airbnb.service.IRoomService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService implements IRoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RoomConverter roomConverter;
    private final ImageRoomRepository imageRoomRepository;
    private final LocationRepository locationRepository;
    private final OrderRepository orderRepository;
    private final FeedbackRepository feedbackRepository;
    private final GeocodingService geocodingService;
    private final AddressRepository addressRepository;
    private final CloudinaryService cloudinaryService;
    private final FeedbackConverter feedbackConverter;

    @Autowired
    public RoomService(
            RoomRepository roomRepository,
            UserRepository userRepository,
            RoomConverter roomConverter,
            ImageRoomRepository imageRoomRepository,
            LocationRepository locationRepository,
            OrderRepository orderRepository,
            FeedbackRepository feedbackRepository,
            GeocodingService geocodingService,
            AddressRepository addressRepository,
            CloudinaryService cloudinaryService,
            FeedbackConverter feedbackConverter
    ) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.roomConverter = roomConverter;
        this.imageRoomRepository = imageRoomRepository;
        this.locationRepository = locationRepository;
        this.orderRepository = orderRepository;
        this.feedbackRepository = feedbackRepository;
        this.geocodingService = geocodingService;
        this.addressRepository = addressRepository;
        this.cloudinaryService = cloudinaryService;
        this.feedbackConverter = feedbackConverter;
    }

    @Override
    public List<RoomDTO> findAll() {
        List<RoomDTO> rooms = roomConverter.toDTOs(roomRepository.findAll());
        for (RoomDTO room : rooms) {
            room.setTotalStar(averageStar(room.getId()));
        }
        return rooms;
    }

    @Override
    public RoomDTO findOneById(long id) {
        RoomDTO roomDTO = roomConverter.toDTO(roomRepository.findOneById(id));
        roomDTO.setTotalStar(averageStar(id));
        return roomDTO;
    }

    @Transactional
    @Override
    public RoomDTO save(RoomDTO dto, long idUser) throws IOException, InterruptedException, ApiException {
        RoomEntity roomEntity = roomConverter.toEntity(dto);
        roomEntity.setUser(userRepository.findOneById(idUser));
        roomEntity.setLocation(locationRepository.findOneByCode(dto.getCodeLocation()));
        // Đặt múi giờ cho Việt Nam
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        // Lấy thời điểm hiện tại theo múi giờ Việt Nam
        ZonedDateTime nowInVietnam = ZonedDateTime.now(zoneId);
        // Chuyển đổi thành LocalDateTime
        LocalDateTime localDateTime = nowInVietnam.toLocalDateTime();
        // Đặt thời điểm cho roomEntity
        roomEntity.setCreated_at(localDateTime);
        AddressEntity addressEntity = roomEntity.getAddress();
        LatLng latLng = geocodingService.getLatLngFromAddress(addressEntity.getFullAddress());
        if (latLng == null) {
            addressEntity.setLat(0);
            addressEntity.setLng(0);
        } else {
            // Lưu tọa độ vào đối tượng Address
            addressEntity.setLat(latLng.lat);
            addressEntity.setLng(latLng.lng);
        }
        // Lưu đối tượng Address vào cơ sở dữ liệu
        addressRepository.save(addressEntity);
        RoomEntity saveRoom = roomRepository.save(roomEntity);
        List<ImageRoomEntity> imageRoomEntities = new ArrayList<>();
        for (String image : dto.getImages()) {
            imageRoomEntities.add(
                    imageRoomRepository.save(ImageRoomEntity.builder()
                            .urlImage(image)
                            .room(saveRoom)
                            .build()));
        }
        saveRoom.setImages(imageRoomEntities);
        return roomConverter.toDTO(saveRoom);
    }

    @Override
    public RoomDTO update(RoomDTO dto) throws IOException, InterruptedException, ApiException {
        updateImages(dto);
        updateAddress(dto);

        RoomEntity roomEntityNew = roomConverter.toEntity(dto, roomRepository.findOneById(dto.getId()));
        return roomConverter.toDTO(roomRepository.save(roomEntityNew));
    }

    private void updateImages(RoomDTO dto) {
        if (dto.getImages().size() == 5) {
            List<ImageRoomEntity> imageRoomEntities = imageRoomRepository.findAllByRoomId(dto.getId());
            for (int i = 0; i < imageRoomEntities.size(); i++) {
                cloudinaryService.deleteImage(imageRoomEntities.get(i).getUrlImage());
                int imageIndex = i;
                imageRoomRepository.save(ImageRoomEntity.builder()
                        .id(imageRoomEntities.get(i).getId())
                        .urlImage(dto.getImages().get(i))
                        .room(roomRepository.findOneById(dto.getId()))
                        .build());
            }
        }
    }

    private void updateAddress(RoomDTO dto) throws IOException, InterruptedException, ApiException {
        RoomEntity roomEntityOld = roomRepository.findOneById(dto.getId());

        if (!dto.getAddress().getFullAddress().equalsIgnoreCase(roomEntityOld.getAddress().getFullAddress())) {
            LatLng latLng = geocodingService.getLatLngFromAddress(dto.getAddress().getFullAddress());
            AddressEntity addressEntityNew = new AddressEntity();
            addressEntityNew.setId(roomEntityOld.getAddress().getId());
            addressEntityNew.setFullAddress(dto.getAddress().getFullAddress());
            addressEntityNew.setLat(latLng != null ? latLng.lat : 0);
            addressEntityNew.setLng(latLng != null ? latLng.lng : 0);
            roomEntityOld.setAddress(addressRepository.save(addressEntityNew));
        }
    }

    @Override
    public List<RoomDTO> search(SearchHouseRequest request) {
        List<RoomDTO> roomDTOS = roomConverter.toDTOs(roomRepository.findByLocation_Id(request.getIdLocation()));
        for (RoomDTO roomDTO : roomDTOS) {
            if (request.getGuests() <= roomDTO.getMaxGuests()) {
                List<DayBooking> bookingDates = checkDateOfRoom(roomDTO.getId());
                roomDTO.setAvailable(isBookingConflict(bookingDates, request.getStartDate(), request.getEndDate()));
            }
        }
        return roomDTOS;
    }

    @Override
    public List<DayBooking> checkDateOfRoom(long idRoom) {
        List<DayBooking> result = new ArrayList<>();
        List<OrderEntity> orderEntities = orderRepository.findAllByRoomId(idRoom);
        for (OrderEntity orderEntity : orderEntities) {
            if (!orderEntity.getStatus().equals(Order.CANCEL)) {
                result.add(DayBooking.builder()
                        .startDate(orderEntity.getReceivedDate())
                        .endDate(orderEntity.getCheckoutDate())
                        .build());
            }
        }
        return result;
    }

    @Override
    public String delete(Long idRoom) {
        RoomEntity roomEntity = roomRepository.findOneById(idRoom);
        if (roomEntity == null) {
            return "Room does not exist";
        }
        roomRepository.delete(roomEntity);
        return "Room deleted successfully";
    }

    @Override
    public List<FeedbackDTO> findAllFeedbackByIDRoom(Long idRoom) {
        return feedbackConverter.mapperTOEntity(feedbackRepository.findByOrder_Room_Id(idRoom));
    }

    public boolean isBookingConflict(List<DayBooking> list, LocalDate startDate, LocalDate endDate) {
        for (DayBooking booking : list) {
            if ((startDate.isAfter(booking.getStartDate()) && startDate.isBefore(booking.getEndDate())) ||
                    (endDate.isAfter(booking.getStartDate()) && endDate.isBefore(booking.getEndDate())) ||
                    (startDate.equals(booking.getStartDate()) && endDate.equals(booking.getEndDate())) ||
                    (startDate.equals(booking.getStartDate()) && endDate.isBefore(booking.getEndDate())) ||
                    (startDate.equals(booking.getStartDate()) && endDate.isAfter(booking.getEndDate())) ||
                    (startDate.isBefore(booking.getStartDate()) && endDate.equals(booking.getEndDate())) ||
                    (startDate.isAfter(booking.getStartDate()) && endDate.equals(booking.getEndDate())) ||
                    (startDate.isBefore(booking.getStartDate()) && endDate.isAfter(booking.getEndDate()))) {
                return false; // Trùng lịch
            }
        }
        return true; // Không trùng lịch
    }

    private double averageStar(Long idRoom) {
        double result = 0;
        List<FeedbackEntity> feedbackEntities = feedbackRepository.findByOrder_Room_Id(idRoom);
        for (FeedbackEntity feedbackEntity : feedbackEntities) {
            result += feedbackEntity.getNumberOfStars();
        }
        return Math.round((result / feedbackEntities.size()) * 100.0) / 100.0;
    }
}
