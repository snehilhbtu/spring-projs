package com.springboot.blog.utils;

public class AppConstants {

    public static final String PAGE_NO_DEFAULT_VALUE="0";
    public static final String PAGE_SIZE_DEFAULT_VALUE="10";
    public static final String SORT_BY_DEFAULT_VALUE="id";
    public static final String ORDER_BY_DEFAULT_VALUE="asc";
    public static final String COMMENT_AND_POST_DONT_RELATE="Comment Doesn't Belongs to That post id";

}


/*
    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username must be 4–20 characters long")
    private String username;

    @Email(message = "Please enter a valid email address")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phone;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 60, message = "Age must not exceed 60")
    private int age;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @Future(message = "Subscription expiry must be in the future")
    private LocalDate subscriptionExpiry;

    @Digits(integer = 6, fraction = 2, message = "Salary must be a valid monetary amount")
    @Positive(message = "Salary must be greater than 0")
    private BigDecimal salary;

    @AssertTrue(message = "You must accept the terms and conditions")
    private boolean agreedToTerms;

    @Null(message = "This internal field must be null")
    private String internalUseOnly;
 */