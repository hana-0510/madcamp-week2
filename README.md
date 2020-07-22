# madcamp-week2

splash screen: 처음 앱에 들어가면 뜨는 페이지로 필요한 권한 (Read_external_storage, contact 등)의 permission을 받음 권한을 허락해주지 않으면 앱이 자동적으로 종료됨

tab 1: database에서 연락처 정보 (이름, 번호, 사진)를 받아와 display함. 연락처를 더하고 지울 수 있슴. 각 연락처를 클릭하면 그 연락처를 확대된 페이지에서 보여주는데
여기에서 이름이나 번호를 edit할 수 있음 

tab 2: database에 저장되어있는 사진을 받아와 display함. 버튼을 누르면 이미지를 갤러리에서 받아오거나 사진을 찍어서 database에 저장할 수 있고 
저장된 즉시 display됨. 이미지를 꾹 누르면 이미지를 지울 수 있음. 이미지를 클릭하면 image clicked activity로 넘어가면서 뷰페이저로 슬라이드 이동 가능함

tab 3: database에서 todo list를 받아와 display함. 버튼을 누르면 alertdialog와 datepickerdialog를 이용하여 새로운 todo를 더함. 
checkbox를 누르면 todo list가 완료되어 db에서 지워지고 날짜를 누르면 due date를 바꿀 수 있고 checkbox옆에 edit button을 누르면 todo내용을 바꿀 수 있음.
