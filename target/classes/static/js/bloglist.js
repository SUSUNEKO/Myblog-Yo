// 定义一个函数，用于将数据发送到后台
function postDataToServer(data) {
    fetch('/api/blog', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(data => {
      // 处理后台返回的响应数据
      console.log(data);
    })
    .catch(error => {
      // 处理请求错误
      console.error(error);
    });
  }
  
  // 获取用户填写的数据
  const image = document.getElementById('blogImageInput').files[0];
  const title = document.getElementById('title').value;
  const contents = document.getElementById('editor-container').querySelector('.ql-editor').innerHTML;
  
  // 构建数据对象
  const data = {
    image: image,
    title: title,
    contents: contents
  };
  
  // 发送数据到后台
  postDataToServer(data);
  