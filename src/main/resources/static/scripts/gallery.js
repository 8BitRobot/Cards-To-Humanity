const app = new Vue(
    {
        el: "#vue_div",
        data: {
            cards: [],
            tagged_with_search_term: "",
            top_n_cards: 25,
            title_contains_search_term: "",
            caption_contains_search_term: ""
        },
        methods: {
            fetchCards: function() {
                var self = this;

                // Make an API call to fetch cards using the search terms that the user provided. The ternary operators (?) are used to set empty search terms to null.
                apiGetCards(
                    self.tagged_with_search_term ? self.tagged_with_search_term : null,
                    self.top_n_cards ? self.top_n_cards : null,
                    self.title_contains_search_term ? self.title_contains_search_term : null,
                    self.caption_contains_search_term ? self.caption_contains_search_term : null
                )
                    .then(function(fetchedCards) {
                        self.cards = fetchedCards;
                    })
                    .catch(function() {
                        console.log("Error fetching cards.");
                    });
            }
        },
        // This function runs on initialization.
        mounted: function() {
            var self = this;
            self.fetchCards();
        }
    }
);
