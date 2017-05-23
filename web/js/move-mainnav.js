$( function() {
    _moveMainnav();

    $(window).resize( function() {
        _moveMainnav();
    } );
} );

function _moveMainnav() {
    if(window.matchMedia){
        if(window.matchMedia( "(max-width: 568px)" ).matches) {
            //alert('[matchMedia]--->(max-width: 568px)" ).matches');
            $("#hpb-nav").insertBefore("#hpb-wrapper");
        }
        else {
            //alert('[matchMedia]--->not (max-width: 568px)" ).matches');
            $("#hpb-nav").insertAfter("#hpb-wrapper");
        }
    }
    else {
        if ($(window).width() <= 568) {
            //alert('[not matchMedia]--->(max-width: 568px)" ).matches');
            $("#hpb-nav").insertBefore("#hpb-wrapper");
        }
        else {
            //alert('[not matchMedia]--->not (max-width: 568px)" ).matches');
            $("#hpb-nav").insertAfter("#hpb-wrapper");
        }
    }
}