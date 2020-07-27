const app = new Vue(
    {
        el: "#vue_div",
        data: {
            username_or_email: "",
            password: "",
            error_message: ""
        },
        methods: {
            loginButtonClicked: function() {
                var self = this;

                apiLoginUser(this.username_or_email, this.password)
                    .then(function() {
                        document.cookie = "logged_in=true";
                        window.location.href = "/home.html";
                    })
                    .catch(function() {
                        console.log("Failure.");
                        self.error_message = "Login failed. Please try again.";
                    });
            }
        }
    }
);