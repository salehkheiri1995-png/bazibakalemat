# انتشار «قند پارسی» در کافه بازار

این یادداشت مراحل لازم برای ساخت نسخهٔ نهایی و انتشار در کافه بازار را جمع‌بندی می‌کند.

## ۱. پیش‌نیازهای ساخت

- JDK 17
- Android SDK با `compileSdk = 36.1` (Gradle در صورت نیاز خودش نصب می‌کند)
- اجرای تست‌ها و ساخت نسخهٔ آزمایشی:

```powershell
$env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-17.0.21.21-hotspot"
.\gradlew.bat testDebugUnitTest
.\gradlew.bat assembleDebug
```

خروجی APK آزمایشی: `app/build/outputs/apk/debug/app-debug.apk`

## ۲. تنظیم تپسل (Tapsell)

کلید اپلیکیشن از طریق `TapsellMediationAppKey` در مانیفست ادغام می‌شود.
مقدار پیش‌فرض، کلید عمومی نمونهٔ تپسل است و **برای انتشار باید تغییر کند**.

شناسهٔ زون‌های بنر و جایزه‌ای از `BuildConfig` خوانده می‌شوند که از محیط متغیر
پر می‌شوند (مقادیر فعلی، زون‌های نمونهٔ رسمی هستند):

| متغیر | کاربرد |
|--------|--------|
| `TAPSELL_APP_KEY` | کلید اپ |
| `TAPSELL_BANNER_ZONE_ID` | زون بنر |
| `TAPSELL_REWARDED_ZONE_ID` | زون ویدیوی جایزه‌ای |

```powershell
$env:TAPSELL_APP_KEY = "<production-app-key>"
$env:TAPSELL_BANNER_ZONE_ID = "<banner-zone>"
$env:TAPSELL_REWARDED_ZONE_ID = "<rewarded-zone>"
```

این سه مقدار را می‌توانید در `secrets.local` هم بگذارید تا اسکریپت ساخت آن‌ها را بردارد.

> جایزه فقط وقتی پرداخت می‌شود که SDK رویداد `onRewarded()` را صدا بزند؛ بستن،
> رد کردن یا شکست در بارگذاری تبلیغ هیچ سکه/الماسی نمی‌دهد.

### ساختن اپ و زون‌ها در پنل تپسل
1. در [panel.tapsell.ir](https://panel.tapsell.ir) ثبت‌نام کنید و یک «اپلیکیشن جدید» بسازید.
2. **کلید اپ** (App Key) را کپی کنید → `TAPSELL_APP_KEY`.
3. یک زون **بنر (Banner)** با سایز معمولی بسازید → `TAPSELL_BANNER_ZONE_ID`.
4. یک زون **ویدیوی جایزه‌ای (Rewarded Video)** بسازید → `TAPSELL_REWARDED_ZONE_ID`.
5. زون‌ها را در وضعیت «فعال» بگذارید و در پنل، بستهٔ اپ
   (`com.aistudio.qandparsi.kzmqpx`) را ثبت کنید.

## ۳. کلید امضای انتشار (ساخته شد)

کلید امضای انتشار ساخته شده و در مکانی امن بیرون از پروژه ذخیره شده است:

```
C:\Keystores\qandparsi-upload.jks
```

- alias: `upload`
- اعتبار: ۱۰٬۰۰۰ روز (~۲۷ سال)
- اعتبارنامه‌ها (رمزها) در `C:\Keystores\.generated_pass.txt` و `secrets.local` ذخیره شده‌اند.

**بیشترین مهم:** از این فایل و رمزها یک بک‌آپ آفلاین (فلش/USB یا گاوصندوق ابری خصوصی)
بگیرید. بعد از اولین انتشار، کافه بازار آپلودهای بعدی را با همین کلید می‌خواهد و
**کلید قابل تغییر نیست**.

### ساخت نسخهٔ انتشار
```powershell
.\build-release.ps1
```
این اسکریپت `secrets.local` را می‌خواند (KEYSTORE_PATH, STORE_PASSWORD, KEY_PASSWORD و
اختیاری: TAPSELL_* و BAZAAR_RSA_PUBLIC_KEY) و `assembleRelease` را اجرا می‌کند.

خروجی: `app/build/outputs/apk/release/app-release.apk`

> `secrets.local` در `.gitignore` است و هرگز نباید در گیت بیاید.

## ۴. پرداخت درون‌برنامه‌ای (Poolakey)

خرید واقعی از طریق SDK رسمی «کافه بازار» (Poolakey) وصل شده است:

- کتابخانه `com.github.cafebazaar.Poolakey:poolakey:2.1.0` از JitPack اضافه شده.
- کلاس `BazaarBillingManager` اتصال به سرویس بازار، شروع پرداخت و بررسی امضا را مدیریت می‌کند.
- بعد از تأیید خرید موفق، `GameViewModel.confirmPurchase` آیتم‌ها (سکه، الماس، VIP، حذف تبلیغ) را اعتبار می‌کند.
- **کلید RSA عمومی** را از پنل کافه بازار بردارید و در زمان ساخت ارائه دهید — هرگز hardcode نکنید:

```powershell
$env:BAZAAR_RSA_PUBLIC_KEY = "<your-rsa-public-key>"
# یا
.\gradlew.bat assembleRelease -PBAZAAR_RSA_PUBLIC_KEY=<your-rsa-public-key>
```

> تا زمانی که کلید RSA تنظیم نشود، خریدها عمداً آغاز نمی‌شوند و هیچ آیتمی اعتبار نمی‌شود
> (بازیکن پیام «پرداخت هنوز پیکربندی نشده» می‌بیند). این رفتار صادقانه است و از اعتبار کردن
> دروغین جلوگیری می‌کند.

## ۵. انتشار در کافه بازار

مراحل انتشار:

1. در [کافه بازار توسعه‌دهندگان](https://pishkhan.cafebazaar.ir) حساب بسازید.
2. «اپلیکیشن جدید» → بستهٔ `com.aistudio.qandparsi.kzmqpx` (همان `applicationId`).
3. APK امضاشده (`app-release.apk`) را آپلود کنید.
4. عنوان، توضیح، آیکون، اسکرین‌شات و دسته‌بندی را وارد کنید.
5. «سیاست حریم خصوصی» که استفاده از شناسهٔ تبلیغاتی (Tapsell) و پرداخت درون‌برنامه‌ای را توضیح دهد پیوست کنید.
6. قبل از هر آپلود جدید `versionCode` را در `app/build.gradle.kts` افزایش دهید (نسخهٔ فعلی `1` است).

نکات:
- `namespace` و بستهٔ کد `com.aistudio.qandparsi` است؛ `applicationId` `com.aistudio.qandparsi.kzmqpx`
  است. شناسهٔ بسته پس از اولین انتشار قابل تغییر نیست؛ اگر نام برند خود را می‌خواهید،
  **قبل از اولین انتشار** `applicationId` را در `app/build.gradle.kts` تغییر دهید.
- کلید امضای `C:\Keystores\qandparsi-upload.jks` را نگه دارید؛ آپدیت‌های بعدی با همین کلید امضا می‌شوند.

## ۶. بررسی سریع پیش از انتشار

- [ ] تست‌های واحد سبز هستند (`testDebugUnitTest`).
- [ ] `TAPSELL_APP_KEY`، `TAPSELL_BANNER_ZONE_ID`، `TAPSELL_REWARDED_ZONE_ID` تولیدی در `secrets.local` گذاشته شده‌اند.
- [ ] بنر در صفحهٔ اصلی و فروشگاه دیده می‌شود و در صفحهٔ بازی مخفی است.
- [ ] ویدیوی جایزه‌ای فقط پس از `onRewarded()` جایزه می‌دهد.
- [ ] `BAZAAR_RSA_PUBLIC_KEY` از پنل بازار در `secrets.local` گذاشته شده است.
- [ ] خرید واقعی روی یک دستگاه با بازار نصب‌شده تست شده است.
- [ ] کشیدن انگشت به سمت چپ، حرف سمت چپ چرخ را انتخاب می‌کند.
- [ ] `.\build-release.ps1` اجرا و `app-release.apk` بدون خطا با کلید انتشار امضا شده است.
- [ ] بک‌آپ آفلاین از `qandparsi-upload.jks` + رمزها گرفته شده است.
- [ ] `versionCode` مناسب است و APK جدید است.
