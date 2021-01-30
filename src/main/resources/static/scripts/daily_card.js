const app = new Vue(
    {
        el: "#vue_div",
        data: {
            email: "",
            first_name: "",
            last_name: ""
        },
        methods: {
            submitButtonClicked: function() {
                apiSignUpEmail(this.email, this.first_name, this.last_name).then(function() {
                    window.location.href = "/home.html";
                });
            }
        }
    }
);
