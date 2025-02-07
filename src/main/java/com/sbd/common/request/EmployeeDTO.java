package com.sbd.common.request;

import lombok.Data;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class EmployeeDTO {

    private DepartmentDTO departmentDTO;
    private EmployeeAttendanceDTO employeeAttendanceDTO;
    private EmployeeDetailsDTO employeeDetailsDTO;
    private LeaveDTO leaveDTO;
    private LeaveTypeDTO leaveTypeDTO;
    private RoleDTO roleDTO;
    private UserCredentialsDTO userCredentialsDTO;


    // Department
    @Data
    public static class DepartmentDTO {
        private Integer id;
        private String name;
    }

    // EmployeeAttendance
    @Data
    public static class EmployeeAttendanceDTO {
        private Integer id;
        private Integer employeeId;
        private Integer departmentId;
        private Integer roleId;
        private LocalDate date;
        private LocalTime checkinTime;
        private LocalTime checkoutTime;
        private BigDecimal workingHours;
        private BigDecimal overtime;
        private String shiftDetails;
        private String location;
        private String photo;
        private String approvalStatus;
        private String status;
        private Integer leaveId;
    }

    // EmployeeDetails
    @Data
    public static class EmployeeDetailsDTO {
        private Integer id;
        private String employeeName;
        private String guardianName;
        private String aadharNumber;
        private String pancard;
        private LocalDate dob;
        private String gender;
        private String phoneNumber;
        private String emergencyNumber;
        private String nationality;
        private String bloodGroup;
        private String addressLine1;
        private String addressLine2;
        private String state;
        private String district;
        private String postalCode;
        private Integer roleId;
        private Integer experience;
        private LocalDate dateOfJoining;
        private FileUpload profilePic;
        private FileUpload aadharPic;
        private FileUpload pancardPic;
        private String status;
        private String approvalStatus;
        private Integer departmentId;
    }

    // Leave DTO
    @Data
    public static class LeaveDTO {
        private Integer id;
        private Integer employeeId;
        private Integer leaveTypeId;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer departmentId;
        private String reason;
    }


    // LeaveType
    @Data
    public static class LeaveTypeDTO {
        private Integer id;
        private String type;
    }

    // Role
    @Data
    public static class RoleDTO {
        private Integer id;
        private Integer roleId;
        private String roleName;
        private String createdBy;
        private LocalDateTime creationDate;
        private String status;
    }


    @Data
    public static class ImageDTO {
        private Integer id;
        private FileUpload profilePic;
        private FileUpload aadharPic;
        private FileUpload pancardPic;

    }

}

