/*
* @Author: bs
* @Date:   2019-10-15 11:25:57
* @Last Modified by:   bs
* @Last Modified time: 2019-10-15 11:25:59
* 
* 
* 
*/
(function ($) {
	$.extend($.validator.messages, {
		required: "This field is required.",
		remote: "Please fix this field.",
		email: "Please enter a valid email address.",
		url: "Please enter a valid URL.",
		date: "Please enter a valid date.",
		dateISO: "Please enter a valid date (ISO).",
		number: "Please enter a valid number.",
		digits: "Please enter only digits.",
		equalTo: "Please enter the same value again.",
		maxlength: $.validator.format( "Please enter no more than {0} characters." ),
		minlength: $.validator.format( "Please enter at least {0} characters." ),
		rangelength: $.validator.format( "Please enter a value between {0} and {1} characters long." ),
		range: $.validator.format( "Please enter a value between {0} and {1}." ),
		max: $.validator.format( "Please enter a value less than or equal to {0}." ),
		min: $.validator.format( "Please enter a value greater than or equal to {0}." ),
		errorMessage: "The information you filled in is wrong.",
		userName: "Chinese characters, English letters, Numbers and underscores.",
		realName: "Can only be 2-30 Chinese characters",
		abc: "Please enter alphanumeric or underline",
		noEqualTo: "Please enter different values again",
		mobile: "Please fill in your mobile number correctly, only 13,14,15,16,17,18,19",
		simplePhone: "Please fill in your phone number correctly, and the fixed number is the area code (3-4 digits),",
		phone: "Please fill in your phone number correctly, the number (3-4 digits), and the phone number is 13,14,15,16,17,18,19.",
		zipCode: "Please fill in your zipCode correctly",
		integer: "Please enter an integer",
		ipv4: "Please enter a valid IP v4 address",
		ipv6: "Please enter a valid IP v6 address",
		qq: "Please fill in your qq number correctly",
		idcard: "Please enter the correct id number (15-18)"
	});
}(jQuery));
