function disableOponentElements(player){
	console.log("here");
	if(player == "PLAYER_1"){
		$(".PLAYER_2").css("pointer-events", "none");
		$(".PLAYER_1").addClass("selectable");
	}else {
		$(".PLAYER_1").css("pointer-events", "none");
		$(".PLAYER_2").addClass("selectable");
	}
}
function setText(text) {
	$(".alert").text(text).css('font-weight','bold');
}

$.ajax({
	url : '/game',
	type : 'GET',
	error : function() {
		console.log("error");
	},
	success : function(data) {
		$.each(data.board.pits, function(i, pit) {
			console.log(pit);
			if (pit.stones != 0) {
				$('#pit' + pit.id).text(pit.stones)
			} else {
				$('#pit' + pit.id).text("");
			}
		});
	}
});
$(document).on("click", ".pit", function() {
	console.log("clicked");
});

$(document).on("click", ".btn-play", function(e) {
	player = $(e.target).data("player");
	var url = "/register/" + player;
	$.ajax({
		url : url,
		type : 'POST',
		error : function() {
			console.log("error");
		},
		success : function(data){
			$(".btn-play").hide();
			$(".btn-playPit").show();
			$('.alert').removeClass('alert-primary');
			$('.alert').addClass('alert-success');
			setText("waiting for your oponent");
			$(".alert").alert();
			var source = new EventSource("/sse");
			source.onmessage = function(event) {
				console.log(JSON.parse(event.data));
			}
			disableOponentElements(player);
		}
	});
});



