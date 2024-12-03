$(function() {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	alert(header);
	$(document).ajaxSend(function(e, xhr, options) {
		if (options.type == "POST") {
			xhr.setRequestHeader(header, token);
		}
	});
});


