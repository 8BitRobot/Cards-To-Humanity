const app = new Vue(
    {
        el: "#vue_div",
        data: {
            username_or_email: "",
            password: ""
        },
        methods: {
            loginButtonClicked: function() {
                console.log("login");
            }
        }
    }
);