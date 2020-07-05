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
        }
    }
);
