const app = new Vue(
    {
        el: "#vue_div",
        data: {
            tagged_with_search_term: "",
            top_n_cards: 25,
            title_contains_search_term: "",
            caption_contains_search_term: "",
            show_modal: false,
            modal_img: "",
            modal_card_caption: "",
            modal_card_id: -1,
            modal_card_liked: false,
        },
        methods: {
            showModal: function (img) {
                this.show_modal = true;
                this.modal_img = img[0];
                this.modal_card_caption = img[1];
                this.modal_card_id = img[2];
                apiGetCardLiked(this.modal_card_id)
                    .then((resolved) => {
                        console.log(resolved);
                        this.modal_card_liked = resolved;
                    })
                    .catch((rejected) => {
                        console.error("i don't know what's goin on");
                    });
            },
            setDefaults: function () {
                this.modal_img = "";
                this.modal_card_caption = "";
                this.modal_card_liked = false;
            }
        }
    }
);
