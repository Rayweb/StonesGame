function setUIelements(player) {
	switch (player) {
	case "PLAYER_1":
		$(".PLAYER_1").addClass("selectable");
		$('.btn-playPit[data-player="PLAYER_1"]').prop('disabled', false);
		$('.PLAYER_2').css("pointer-events", "none");
		$('.PLAYER_1').css("pointer-events", "auto");
		break;
	case "PLAYER_2":
		$(".PLAYER_2").addClass("selectable");
		$('.btn-playPit[data-player="PLAYER_2"]').prop('disabled', false);
		$('.PLAYER_1').css("pointer-events", "none");
		$('.PLAYER_2').css("pointer-events", "auto");
		break;
	default:
		console.log("something when wrong here");
	}
}

function disableUI() {
	$(".selectable").removeClass("selectable");
	$(".selectedPit").removeClass("selectedPit");
	$('.btn-playPit').prop('disabled', true);
}

function setMessage(text, type) {
	$(".alert").text(text).css('font-weight', 'bold');
	$('.alert').removeClass(
			'alert-primary alert-danger alert-success alert-warning');
	$('.alert').addClass('alert-' + type);
	$(".alert").alert();
}

function setInitialUI() {
	$("#activePlayer").text("");
	$(".btn-playPit").hide();
	$(".btn-play").show();
	$(".selectable").removeClass("selectable");
	$(".selectedPit").removeClass("selectedPit");
}

function updatePits(pits){
	$.each(pits, function(i, pit) {
		if (pit.stones !== 0) {
			$('#pit' + pit.id).text(pit.stones)
		} else {
			$('#pit' + pit.id).text("");
			$('#pit' + pit.id).closest(".pit").removeClass(
					"selectable selectedPit");
			$('#pit' + pit.id).closest(".pit").css("pointer-events", "none");
		}
	});
}

function drawBoard(game) {
	switch (game.state) {
	case "READY":
		setMessage("A player joined, game will start once you select your player", "success");
		break;
	case "FINISHED":
		var winner = game.winner;
		disableUI();
		setMessage("The Winner is " + winner + "!!!", "warning");
		break;
	case "RESTARTED":
		setInitialUI();
		setMessage("Game restarted, lets play again! Select your player.","primary");
		break;
	default:
		var activePlayer = $("#activePlayer").text();
		if (game.nextTurn == activePlayer) {
			setUIelements(game.nextTurn);
		} else {
			disableUI();
		}
		setMessage("Its " + game.nextTurn + " Turn", "success");
	}
	updatePits(game.board.pits);
}

function playerRegisted(message, player) {
	$(".btn-play, .btn-playPit").toggle();
	setMessage(message, "success");
	$("#activePlayer").text(player)
}

var source = new EventSource("/sse");
source.onmessage = function(event) {
	drawBoard(JSON.parse(event.data));
}

$(document).on("click", ".selectable", function(e) {
	$(".selectedPit").addClass("selectable");
	$(".selectedPit").removeClass("selectedPit");
	$(e.target).closest(".pit").addClass("selectedPit");
});

$(document).on("click", ".btn-play", function(e) {
	var player = $(e.target).data("player");
	var url = "/register/player/" + player;
	$.ajax({
		url : url,
		type : 'POST',
		error : function(request, status, error) {
			setMessage(request.responseJSON.message, "danger");
		},
		success : function(data) {
			playerRegisted(data, player);
		}
	});
});

$(document).on("click", ".btn-playPit", function(e) {
	var player = $(e.target).data("player");
	var pitId = $(".selectedPit span").data("id");
	if (pitId == undefined) {
		setMessage("Please select a pit first!", "danger");
	} else {
		var url = "/playTurn/" + player + "/" + pitId;
		$.ajax({
			url : url,
			type : 'POST',
			error : function(request, status, error) {
				setMessage(request.responseText, "danger");
			},
			success : function() {
				console.log("Move applied");
			}
		});
	}

});

$(document).on("click",".reset-game",function(e) {
	$('#myModal').modal('hide');
	$.ajax({
		url : "/reset",
		type : 'POST',
		error : function(request, status, error) {
			setMessage(request.responseText, "danger");
		},
		success : function() {
			console.log('Restarted')
		}
	});
});
