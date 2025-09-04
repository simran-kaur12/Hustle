package com.example.hustle.models;

public class MainModel {
    String Arrival_Time,Departure_Time,Travel_Agency,Fare,Duration,Category,Bus_No,Source,Destination,Bus_Id;

    MainModel(){
    }
    public MainModel(String arrival_Time, String departure_Time, String travel_Agency, String fare, String duration, String category, String bus_No, String bus_Id) {
        Arrival_Time = arrival_Time;
        Departure_Time = departure_Time;
        Travel_Agency = travel_Agency;
        Fare = fare;
        Duration = duration;
        Category = category;
        Bus_No = bus_No;
        Bus_Id = bus_Id;
    }

    public String getBus_Id() {
        return Bus_Id;
    }

    public String getSource() {
        return Source;
    }

    public String getDestination() {
        return Destination;
    }

    public String getArrival_Time() {
        return Arrival_Time;
    }

    public String getTravel_Agency() {
        return Travel_Agency;
    }

    public String getFare() {
        return Fare;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getCategory() {
        return Category;
    }

    public String getBus_No() {
        return Bus_No;
    }

}
