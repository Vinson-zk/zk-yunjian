/*
* @Author: Vinson
* @Date:   2020-02-24 01:05:25
* @Last Modified by:   Vinson
* @Last Modified time: 2020-03-10 13:03:12
* 
* 
* 
*/
(function(_jq) {

	var superErrorPlacement = _jq.validator.defaults.errorPlacement;
	var superSuccess = _jq.validator.defaults.success;

	// var superErrorPlacement = function(b, c) {
	// 	if (c.closest(".icheck").length > 0) {
	// 		c = c.closest(".icheck");
	// 		c.parent().css("position", "relative");
	// 		b.insertAfter(c);
	// 		b.css({
	// 			top: c.position().top + c.outerHeight() + 2,
	// 			left: c.position().left + 5
	// 		});
	// 		return
	// 	}
	// 	if (c.next().hasClass("select2")) {
	// 		c = c.next();
	// 		c.parent().css("position", "relative")
	// 	} else {
	// 		if (c.closest(".input-group").length > 0) {
	// 			c = c.closest(".input-group");
	// 			c.parent().css("position", "relative")
	// 		}
	// 	}
	// 	b.insertAfter(c);
	// 	b.css({
	// 		top: c.position().top + c.outerHeight() - 5,
	// 		left: c.position().left + 5
	// 	})
	// }

	_jq.extend(_jq.validator.defaults, {
		errorClass:"invalid-feedback",
		success: function(error, element){
			// console.log("[^_^:20200224-0123-001] success: ", element);
			$(element).removeClass('is-invalid');
			$(element).addClass('is-valid');
			superSuccess(error, element);
		},
		errorPlacement: function(error, element){
			// console.log("[^_^:20200224-0123-001] errorPlacement: ", error, element);
			element.addClass("is-invalid");
			superErrorPlacement(error, element);
		}

	});
}(jQuery));