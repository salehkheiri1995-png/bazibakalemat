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

```powershell
$env:TAPSELL_APP_KEY = "<production-app-key>"
# یا
.\gradlew.bat assembleRelease -PTAPSELL_APP_KEY=<production-app-key>
```

شناسهٔ زون‌های بنر و جایزه‌ای در فایل زیر است و باید با زون‌های ساخته‌شده
در پنل تپسل برای همین اپ جایگزین شود:

`app/src/main/java/com/example/ads/AdConfig.kt`

- بنر: `BANNER_ZONE_ID`
- ویدیوی جایزه‌ای: `REWARDED_ZONE_ID`

جایزه فقط وقتی پرداخت می‌شود که SDK رویداد `onRewarded()` را صدا بزند؛ بستن،
رد کردن یا شکست در بارگذاری تبلیغ هیچ سکه/الماسی نمی‌دهد.

## ۳. امضای نسخهٔ انتشار

تنظیمات امضای AI Studio در `app/build.gradle.kts` دست‌نخورده باقی مانده است.
برای ساخت نسخهٔ نهایی این متغیرهای محیطی را تنظیم کنید:

```powershell
$env:KEYSTORE_PATH = "C:\path\to\my-upload-key.jks"
$env:STORE_PASSWORD = "<store-password>"
$env:KEY_PASSWORD = "<key-password>"
.\gradlew.bat assembleRelease
```

خروجی: `app/build/outputs/apk/release/app-release.apk`

> کلید امضا را گم نکنید؛ برای آپدیت‌های بعدی در بازار به همان کلید نیاز دارید.

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

## ۵. نکات کافه بازار

- `namespace` و بستهٔ کد `com.aistudio.qandparsi` است؛ `applicationId` فعلی
  `com.aistudio.qandparsi.kzmqpx` است. شناسهٔ بسته پس از اولین انتشار قابل تغییر نیست؛ اگر نام
  برند خود را می‌خواهید، **قبل از اولین انتشار** آن را در `app/build.gradle.kts` تغییر دهید.
- قبل از هر انتشار `versionCode` را افزایش دهید.
- سیاست حریم خصوصی و توضیح استفاده از شناسهٔ تبلیغاتی (Tapsell) و پرداخت درون‌برنامه‌ای را در
  صفحهٔ محصول بازار قرار دهید.

## ۶. بررسی سریع پیش از انتشار

- [ ] تست‌های واحد سبز هستند (`testDebugUnitTest`).
- [ ] کلید و زون‌های تولیدی تپسل جایگزین شده‌اند.
- [ ] کلید RSA عمومی بازار در Build تنظیم شده است (`BAZAAR_RSA_PUBLIC_KEY`).
- [ ] بنر در صفحهٔ اصلی و فروشگاه دیده می‌شود و در صفحهٔ بازی مخفی است.
- [ ] ویدیوی جایزه‌ای فقط پس از `onRewarded()` جایزه می‌دهد.
- [ ] کشیدن انگشت به سمت چپ، حرف سمت چپ چرخ را انتخاب می‌کند.
- [ ] خرید واقعی روی یک دستگاه با بازار نصب‌شده تست شده است.
- [ ] `versionCode` افزایش یافته و APK با کلید انتشار امضا شده است.
