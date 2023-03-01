package com.example.adminapp.pojo;

import java.io.Serializable;

public class NurseAddtoCart implements Serializable {

    private String nurseTakenId;
    private String nurseTakenDate;
    private String nurseTakenPrectionImage;
    private String nurseTakenDoctorRef;
    private NurseVendorDetails vendorDetails;
    private NursePackageDetails packageDetails;
    private PatientDetails patientDetails;
    private String status;

    public NurseAddtoCart() {

    }

    public NurseAddtoCart(String nurseTakenId, String nurseTakenDate, String nurseTakenPrectionImage, String nurseTakenDoctorRef, NurseVendorDetails vendorDetails, NursePackageDetails packageDetails, PatientDetails patientDetails, String status) {
        this.nurseTakenId = nurseTakenId;
        this.nurseTakenDate = nurseTakenDate;
        this.nurseTakenPrectionImage = nurseTakenPrectionImage;
        this.nurseTakenDoctorRef = nurseTakenDoctorRef;
        this.vendorDetails = vendorDetails;
        this.packageDetails = packageDetails;
        this.patientDetails = patientDetails;
        this.status = status;
    }

    public String getNurseTakenId() {
        return nurseTakenId;
    }

    public void setNurseTakenId(String nurseTakenId) {
        this.nurseTakenId = nurseTakenId;
    }

    public String getNurseTakenDate() {
        return nurseTakenDate;
    }

    public void setNurseTakenDate(String nurseTakenDate) {
        this.nurseTakenDate = nurseTakenDate;
    }

    public String getNurseTakenPrectionImage() {
        return nurseTakenPrectionImage;
    }

    public void setNurseTakenPrectionImage(String nurseTakenPrectionImage) {
        this.nurseTakenPrectionImage = nurseTakenPrectionImage;
    }

    public String getNurseTakenDoctorRef() {
        return nurseTakenDoctorRef;
    }

    public void setNurseTakenDoctorRef(String nurseTakenDoctorRef) {
        this.nurseTakenDoctorRef = nurseTakenDoctorRef;
    }

    public NurseVendorDetails getVendorDetails() {
        return vendorDetails;
    }

    public void setVendorDetails(NurseVendorDetails vendorDetails) {
        this.vendorDetails = vendorDetails;
    }

    public NursePackageDetails getPackageDetails() {
        return packageDetails;
    }

    public void setPackageDetails(NursePackageDetails packageDetails) {
        this.packageDetails = packageDetails;
    }

    public PatientDetails getPatientDetails() {
        return patientDetails;
    }

    public void setPatientDetails(PatientDetails patientDetails) {
        this.patientDetails = patientDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
