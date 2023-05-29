// +++++++++blogList页面的设定+++++++
  // DELETE BOTTON
const deleteButton = document.getElementById("deleteButton");
const confirmDialog = document.getElementById("confirmDialog");
const noButton = document.getElementById("noButton");
const yesButton = document.getElementById("yesButton");

deleteButton.addEventListener("click", function() {
  confirmDialog.style.display = "flex";
});

noButton.addEventListener("click", function() {
  confirmDialog.style.display = "none";
});

yesButton.addEventListener("click", function() {
  // 删除操作或其他逻辑?
  confirmDialog.style.display = "none";
});

  
  // +++++++++++login.html++++++++++++++++
  function resizeInput() {
    var input = document.getElementById("myInput");
    input.style.width = (input.value.length + 1) + "ch"; // 根据输入内容的长度调整宽度
    input.style.height = (input.scrollHeight) + "px"; // 根据输入内容的行数调整高度
  }
  

  



 