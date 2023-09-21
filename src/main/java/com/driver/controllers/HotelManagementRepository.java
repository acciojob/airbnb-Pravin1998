package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Repository
public class HotelManagementRepository {

    HashMap<String,Hotel> hotelDB = new HashMap<>();
    HashMap<Integer,User> userDB = new HashMap<Integer, User>();
    HashMap<String,Booking> bookingDB = new HashMap<>();
    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        Hotel hotel = hotelDB.get(hotelName);
        List<Facility> currentFacilities = hotel.getFacilities();

        for(Facility facility : newFacilities){
            if(currentFacilities.contains(facility)){
                continue;
            }else{
                currentFacilities.add(facility);
            }
        }
        hotel.setFacilities(currentFacilities);
        hotelDB.put(hotelName,hotel);
        return hotel;

    }

    public int getBookings(Integer aadharCard) {
        int ans = 0;
        for(String bookingId : bookingDB.keySet()){
            Booking booking = bookingDB.get(bookingId);
            if(booking.getBookingAadharCard() == aadharCard){
                ans++;
            }
        }
        return ans;
    }

    public String addHotel(Hotel hotel) {
        String key = hotel.getHotelName();
        if(key == null){
            return "FAILURE";
        }else if(hotelDB.containsKey(key)){
            return "FAILURE";
        }else{
            hotelDB.put(key,hotel);
        }
        return "SUCCESS";
    }

    public Integer addUser(User user) {
        int key = user.getaadharCardNo();
        userDB.put(key,user);
        return key;
    }

    public String getHotelWithMostFacilities() {
        int noOfFacilities = 0;
        String ans = "";

        for(String hotelName : hotelDB.keySet()){
            Hotel hotel = hotelDB.get(hotelName);
            if(hotel.getFacilities().size() > noOfFacilities){
                ans = hotelName;
                noOfFacilities = hotel.getFacilities().size();
            }
            else if(hotel.getFacilities().size() == noOfFacilities){
                if(hotelName.compareTo(ans)<0){
                    ans = hotelName;
                }
            }
        }
        return ans;
    }

    public int bookARoom(Booking booking) {
        UUID uuid = UUID.randomUUID();
        String bookingId = uuid.toString();
        booking.setBookingId(bookingId);

        String hotelName = booking.getHotelName();
        Hotel hotel = hotelDB.get(hotelName);
        int pricePerNight = hotel.getPricePerNight();
        int noOfRooms = booking.getNoOfRooms();
        int availableRooms = hotel.getAvailableRooms();

        if(noOfRooms > availableRooms){
            return -1;
        }
        int amountPaid = noOfRooms * pricePerNight;
        booking.setAmountToBePaid(amountPaid);

        hotel.setAvailableRooms(availableRooms-noOfRooms);
        bookingDB.put(bookingId,booking);
        hotelDB.put(hotelName,hotel);
        return amountPaid;
    }
}
