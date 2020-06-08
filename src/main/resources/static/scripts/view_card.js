const app = new Vue(
    {
        el: "#vue_div",
        data: {
            card: {}
        },
        mounted: function() {
            var self = this;

            var url = new URL(window.location.href);
            var card_id = parseInt(url.searchParams.get("card_id"));

            apiGetCard(card_id)
                .then(function(card) {
                    self.card = card;
                });
        }
    }
);
