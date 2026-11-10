
# tuan6crypto — Implementation Roadmap

## Yêu cầu
- Chỉ một màn hình Home, không cuộn trên Pixel 10 Pro.
- UI giữ nguyên thiết kế đã được kiểm tra.
- New: banner tĩnh.
- Favorites: ETH, BTC.
- All Fluctuations: ETH, BTC, LTC, XMR.
- Giá, phần trăm và biểu đồ phải từ Kraken thật.
- Retrofit + HTTP GET + Kotlin Coroutines.
- Tự cập nhật khoảng 5 giây/lần khi ứng dụng hoạt động.
- Không sử dụng dữ liệu giá hoặc biểu đồ giả.

## Tiến độ
- [x] Bước 1: Tạo project.
- [x] Bước 2: Hoàn thiện UI.
- [ ] Bước 3: Retrofit, HTTP GET, kiểm tra JSON.
- [ ] Bước 4: DTO, Repository, mapping dữ liệu.
- [ ] Bước 5: ViewModel, Coroutines, polling.
- [ ] Bước 6: Gắn dữ liệu vào UI, biểu đồ thật.
- [ ] Bước 7: Kiểm thử, vấn đáp, GitHub.

## Quy tắc
Chỉ chuyển bước khi bước trước chạy và được kiểm chứng.
Không thay đổi cấu trúc hoặc UI đã chốt nếu chưa có lý do.
```

Sau này anh có thể cập nhật các ô `[x]` mỗi khi hoàn thành một bước.

---

## 2. Bước 3 — Hiểu ba thứ trước khi code

| Khái niệm | Hiểu đơn giản |
|---|---|
| Quyền `INTERNET` | Cho ứng dụng sử dụng mạng |
| Retrofit | Thư viện giúp Kotlin gọi HTTP GET dễ dàng |
| JSON | Định dạng dữ liệu Kraken trả về |

Ví dụ anh đã mở URL Kraken trên Chrome. Bây giờ mình muốn **Android làm việc tương tự**, nhưng thay vì hiển thị JSON trên trình duyệt, ứng dụng sẽ nhận JSON trong code.

Điểm thú vị: Quyền `INTERNET` không cần hộp thoại xin phép người dùng như Camera. Đây là quyền thông thường, Android cấp khi cài app. <Cite ref="turn489329search1"/>

Ở Bước 3, chúng ta chỉ gọi API **một lần để kiểm chứng**, chưa cập nhật mỗi 5 giây và chưa hiển thị giá trên UI.

## 3.1. Cấp quyền Internet

Trong Android Studio, mở:

`app → manifests → AndroidManifest.xml`

Ngay bên dưới thẻ `<manifest ...>` và phía trên `<application>`, thêm:

<CodeBlock language="xml" editable>
<uses-permission android:name="android.permission.INTERNET" />
