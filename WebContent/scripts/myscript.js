/**
 * 
 */

$(function() {
		//window size	
	
		var windowWidth = $(window).width();
		
		var windowHeight = $(window).height();
		
		//blocks
		var $topBlock = $("#topHeader");
		
		var $leftBlock = $("#leftSide");
		var $rightBlock = $("#rightSide");
		var $searchBox = $("#searchBox");
		var $middle = $("#middle");
		var $footer = $("#footer");
		var topBlockHeight = $topBlock.height();
		var footBlockHeight = $footer.height();
		var rightBlockWidth = $rightBlock.width();
		var leftBlockWidth = $leftBlock.width();
		
		function setUpBlocSize() {
				
				var allHeight = parseInt(topBlockHeight) + parseInt(footBlockHeight);
				var allWidth = parseInt(leftBlockWidth) + parseInt(rightBlockWidth);
				var leftBlocHeight = parseInt(windowHeight - allHeight);
				
				$leftBlock.css('height',leftBlocHeight+15);
				$middle.css('min-height',leftBlocHeight-110);
				
		
				$rightBlock.css('height',leftBlocHeight+5);
				$('#right-form\\:subRightBlock').css('height',leftBlocHeight+15);
				
				var middleWidth = parseInt(windowWidth - allWidth);
				$middle.css('width',middleWidth-22);
				$searchBox.css('width',middleWidth-22);
				$('#searchWrapper').css('width',middleWidth-20);
				
				var message="La taille de la fentetre est width:"+windowWidth+" height:"+windowHeight+" <br />";
				message+=" top Height :"+topBlockHeight+" footer Height: "+footBlockHeight+"<br />";
				message+=" Middle height "+leftBlocHeight+" width: "+middleWidth+"<br />";
				message+=" width left :"+leftBlockWidth;
				
				//$middle.append('<p>'+message+'</p>');
				//applies for caisse pages
				
				
		}
		
		function styleButtons() {
			$('button, input[type="reset"], input[type="submit"], input[type="button"]')
			.addClass('radiusBorder5');
			
		}
		
		//add zebra style to tables
		function zebraStyle() {
			$('.zebra-style tr:even').addClass('even');
			$('.zebra-style tr:odd').addClass('odd');
			$('.zebra-style tr').mouseover(function(){
				$(this).removeClass('even odd');
				$(this).addClass('hovered');
			});
			$('.zebra-style tr').mouseout(function(){
				$(this).removeClass('hovered');
				$('.zebra-style tr:even').addClass('even');
				$('.zebra-style tr:odd').addClass('odd');
			});
		}
		
		
		setUpBlocSize();
		styleButtons();
		zebraStyle();
		
		$(window).resize(function() {
			//var taille = 'Taille de la fenêtre : ' + $(window).width() +
			//'px x ' + $(window).height() + 'px';
			//alert(taille);
			windowWidth = $(window).width();
			
			windowHeight = $(window).height();
			$topBlock = $("#topHeader");
			$leftBlock = $("#leftSide");
			$rightBlock = $("#rightSide");
			$middle = $("#middle");
			$footer = $("#footer");
			topBlockHeight = $topBlock.height();
			footBlockHeight = $footer.height();
			rightBlockWidth = $rightBlock.width();
			leftBlockWidth = $leftBlock.width();
			
			setUpBlocSize();
			
			
		});
	
		$('.close').click(function(){
			$('.close').parent().fadeOut('slow');
		});
		
		
		$('.auto-close').on('shown',function(){
			
			var closeTime=10;
			var bottomText = '<span class="auto-close-text" style="font-size:10px;display:inline-block;position:absolute;bottom:3px;right:6px;"></span>';
			
			$('.auto-close').append(bottomText);
			$('.auto-close-text').text('Auto Closing in '+closeTime+' S ');
			
			var timerId = setInterval(function(){
				closeTime=closeTime-1;
				$('.auto-close-text').text('Auto Closing in '+closeTime+' S ');
				if(closeTime==0){
					clearInterval(timerId);
				}
			}, 1000);
			
			setTimeout(function(){
				$('.auto-close').fadeOut('slow');
			}, 10000);
			
		});
		
		
		
		//highlight selectedMenu
		/*
		function activateMenu() {
			$(this).prepend($('.fleche'));
			$('.menuElement').removeClass('selectedMenu');
			$(this).addClass('selectedMenu');
		}
		
		$('.menuElement').click(activateMenu);
		
		//une petite animation des element du menu
		$('.menuElement img').click(function(){
			$(this).animate({
				width:'+=1',
				height:'+=1'
			},'fast','linear',function(){
				$(this).animate({
					width:'-=1',
					height:'-=1'
				});
			});
			//activateMenu();
		});*/
		
		//date and time
		/*var days=["Dimanche","Lundi","Mardi","Mercredi","Jeudi","Vendredi","Samedi","Dimanche"];
		function horloge() {
			var laDate = new Date();
			var dat=days[parseInt(laDate.getDay())-1]+" Le "+laDate.getDate()+" / "+laDate.getMonth()+" / "+laDate.getFullYear();
			var h = laDate.getHours() + ":" + laDate.getMinutes() + ":" +
			laDate.getSeconds();
			$('#dateTime').text(dat+" a "+h);
		}
		
		setInterval(horloge, 1000); */
		
	}		
);
