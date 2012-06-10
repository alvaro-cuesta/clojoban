jQuery.fn.center = function () {
    this.css("position","absolute");
    this.css("left", Math.max(0, (($(window).width() - this.outerWidth()) / 2) + 
                                                $(window).scrollLeft()) + "px");
    return this;
}

$(document).ready(function(){
	$(document).keydown(function(e){
		var keyCode = e.keyCode || e.which,
		    code = {left: 37, up: 38, right: 39, down: 40,
		            n: 78, r: 82};
			
		switch (keyCode) {
		    case code.up:
		    	$("a#up")[0].click();
		    	break;
		    case code.down:
		    	$("a#down")[0].click();
		    	break;
		    case code.left:
		    	$("a#left")[0].click();
		    	break;
		    case code.right:
		    	$("a#right")[0].click();
		    	break;
		    case code.r:
		    	$("a#restart")[0].click();
		    	break;
		    case code.n:
		    	$("a#new")[0].click();
		    	break;
		}
	});
});