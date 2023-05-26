
  // +++++++++++login.html++++++++++++++++
  function resizeInput() {
    var input = document.getElementById("myInput");
    input.style.width = (input.value.length + 1) + "ch"; // 根据输入内容的长度调整宽度
    input.style.height = (input.scrollHeight) + "px"; // 根据输入内容的行数调整高度
  }
  
// ++++++++blog editor+++++++
  document.addEventListener("DOMContentLoaded", function() {
    // Set current date
    var currentDate = new Date();
    document.getElementById("current-date").textContent = currentDate.toLocaleDateString();
  
    // Initialize Quill editor
    var editor = new Quill("#editor-container", {
      theme: "snow"
    });
  
    // Handle form submission
    var uploadButton = document.querySelector(".upload-button");
    uploadButton.addEventListener("click", function(event) {
      event.preventDefault();
      var title = document.getElementById("title").value;
      var content = editor.root.innerHTML;
      var currentDate = new Date();
      var timestamp = currentDate.getTime(); // Generate timestamp or use any ID generation logic
  
      // Perform further processing or send data to backend
      console.log("Title:", title);
      console.log("Content:", content);
      console.log("Timestamp:", timestamp);
    });
  });

  // +++++++++blogList页面的设定+++++++
 