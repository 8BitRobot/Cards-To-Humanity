// Vue.js component that shows a grid displaying cards. Props control search terms used to retrieve the cards from the server API.
Vue.component("cards-display", {
    // The data section is for variables internal to the component.
    data: function() {
        return {
            cards: []
        }
    },
    // These props are parameters set on the klass editor by the page that uses it (via Vue.js bindings). They are used to set the search terms for what cards to display.
    props: {
        tagged_with_search_term: String,
        top_n_cards: Number,
        title_contains_search_term: String,
        caption_contains_search_term: String
    },
    // Handle changes to props (made by the outside program) automatically.
    watch: {
        $props: {
            handler() {
                this.fetchCards();
            },
            deep: true,
            immediate: true
        }
    },
    // The methods are functions used to generate parts of the template. They are also used as event handlers.
    methods: {
        fetchCards: function() {
            var self = this;

            // Make an API call to fetch cards using the search terms that the props provide. The ternary operators (?) are used to set empty search terms to null.
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
    // This function runs when the 
    created: function() {
        EventBus.$on("card-liked", (id) => {
            this.cards[this.cards.findIndex(el => el.card_id === id)].likes++;
        });
    },
    // This function runs when the DOM is rendered.
    mounted: function() {
        var self = this;
        self.fetchCards();
    },
    // The template for the actual HTML of the component.
    template: `
        <div id="cards">
            <div class="card" v-for="card in cards" :key="card.card_id">
                <div class="card-info">
                    <p>
                        {{ card.title }} ({{ card.likes }} likes)
                    </p>
                </div>
                <img v-bind:src="card.media_url" v-bind:alt="card.caption" class="card-image">
                <a class="view-button" @click="$emit('show-modal', [card.media_url, card.caption, card.card_id]);">View</a>
            </div>
        </div>
    `
});

Vue.component("card-modal", {
    data: function () {
        return {}
    },
    props: {
        modal_card_id: Number,
        modal_img: String,
        modal_card_caption: String,
        card_liked: Boolean,
    },
    methods: {
        like: function (id) {
            apiLikeCard(id).then((resolved) => {
                this.card_liked = true;
            })
            EventBus.$emit("card-liked", id);
        }
    },
    template: `
        <div id="modal-background">
            <div id="modal-container">
                <div id="close-button" @click="$emit('hide-modal');">
                    <i class="fas fa-times"></i>
                </div>
                <div id="img-container">
                    <img :src="modal_img" :alt="modal_card_caption">
                    <div>{{ modal_card_caption }}</div>
                </div>
                <div id="modal-side-buttons">
                    <button @click="like(modal_card_id)"><i class="fas fa-fw fa-heart" :style="{
                        color: card_liked ? '#EF476F' : 'white',
                        cursor: card_liked ? 'not-allowed' : 'pointer',
                    }"></i></button>
                    <button><i class="fas fa-fw fa-comment"></i></button>
                    <button><i class="fas fa-fw fa-share"></i></button>
                    <a :href="modal_img" download><i class="fas fa-fw fa-download"></i></a>
                </div>
            </div>
        </div>
    `

})
