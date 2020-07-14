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
            modal_img_caption: "",
        },
        methods: {
            showModal: function (img) {
                this.show_modal = true;
                this.modal_img = img[0];
                this.modal_img_caption = img[1];
            }
        }
    }
);
