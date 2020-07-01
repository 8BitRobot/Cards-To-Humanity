const app = new Vue(
    {
        el: "#vue_div",
        data: {
                title: "",
                caption: "",
                tags: "",
                error_message: "",
                tags: "",
                uploaded_image_base64: "/img/create_card/smiley_card_placeholder.png",
        },
        computed: {
            submitButtonDisabled: function() {
                return this.title === "" || this.$refs.file_upload.files.length == 0;
            }
        },
        methods: {
                previewMediaFile: function() {
                        let files = this.$refs.file_upload.files;
                        let reader = new FileReader();
                        if (files && files[0]) {
                            reader.onload = (e) => {
                                this.uploaded_image_base64 = e.target.result;
                                document.querySelector("#uploaded-image-container img").style.width = "90%";
                                document.querySelector("#card-container p").style.display = "none";
                            }
                            let upload = reader.readAsDataURL(files[0]);
                        }
                },
                finishCardCreation: function() {
                    var self = this;

                    if (self.submitButtonDisabled) {
                        return;
                    }

                    apiCreateCard(this.title, this.caption, this.tags, this.$refs.file_upload)
                        .then(function(response) {
                            window.location.href = "/home.html";
                        })
                        .catch(function(error) {
                            self.error_message = error;
                        });
                }
        }
    }
);
