// ++++++++blog editor+++++++
document.addEventListener("DOMContentLoaded", function() {
    // Set current date
    var currentDate = new Date();
    document.getElementById("current-date").textContent = currentDate.toLocaleDateString();
  
    // Initialize Quill editor
    var editor = new Quill("#editor-container", {
      theme: "snow"
    });
  
    // // Handle form submission
    // var uploadButton = document.querySelector(".upload-button");
    // uploadButton.addEventListener("click", function(event) {
    //   event.preventDefault();
    //   var title = document.getElementById("title").value;
    //   var content = editor.root.innerHTML;
    //   var currentDate = new Date();
    //   var timestamp = currentDate.getTime(); // Generate timestamp or use any ID generation logic
  
    //   // Perform further processing or send data to backend
    //   console.log("Title:", title);
    //   console.log("Content:", content);
    //   console.log("Timestamp:", timestamp);
    // });
  });

  // ++++图像挂件++++++++++++++++
  const fileInput = document.getElementById('blogImageInput');
  const previewImage = document.getElementById('previewImage');
  const imgPreviewHolder = document.getElementById('img-preview');
  
  fileInput.addEventListener('change', function(event) {
    const file = event.target.files[0];
  
    const reader = new FileReader();
    reader.onload = function(event) {
        previewImage.style.removeProperty("display");
      previewImage.src = event.target.result;
    };
  
    if (file) {
      reader.readAsDataURL(file);
    }
  });