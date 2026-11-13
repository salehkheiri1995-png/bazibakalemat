Add-Type -AssemblyName System.Drawing
$ErrorActionPreference = "Stop"

function New-WordWheelIcon($size) {
    $bmp = New-Object System.Drawing.Bitmap ($size), ($size)
    $g = [System.Drawing.Graphics]::FromImage($bmp)
    $g.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::AntiAlias
    $g.CompositingQuality = [System.Drawing.Drawing2D.CompositingQuality]::HighQuality
    $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic

    $c = $size / 2.0
    $s = [float]$size

    # --- Background gradient (deep teal) ---
    $bgRect = New-Object System.Drawing.RectangleF 0,0,$s,$s
    $bgBrush = New-Object System.Drawing.Drawing2D.LinearGradientBrush $bgRect, ([System.Drawing.Color]::FromArgb(255,10,58,64)), ([System.Drawing.Color]::FromArgb(255,0,77,64)), 45
    $g.FillRectangle($bgBrush, 0, 0, [int]$s, [int]$s)

    # --- Wheel slices ---
    $sliceColors = @(
        [System.Drawing.Color]::FromArgb(255,255,193,7),   # gold
        [System.Drawing.Color]::FromArgb(255,38,198,218),   # cyan
        [System.Drawing.Color]::FromArgb(255,102,187,106),   # green
        [System.Drawing.Color]::FromArgb(255,255,159,67),    # amber
        [System.Drawing.Color]::FromArgb(255,92,107,192),    # indigo
        [System.Drawing.Color]::FromArgb(255,255,112,67),    # coral
        [System.Drawing.Color]::FromArgb(255,236,64,122),    # pink
        [System.Drawing.Color]::FromArgb(255,212,163,115)    # sand
    )
    $outerR = $c * 0.78
    $innerR = $c * 0.30
    $seg = 45.0
    for ($i = 0; $i -lt 8; $i++) {
        $start = -90.0 + ($i * $seg) - 1.0
        $sweep = $seg + 2.0
        $brush = New-Object System.Drawing.SolidBrush $sliceColors[$i]
        $g.FillPie($brush, [float]($c - $outerR), [float]($c - $outerR), [float]($outerR*2), [float]($outerR*2), [float]$start, [float]$sweep)
        $brush.Dispose()
    }

    # --- separator lines from hub to rim ---
    $sep = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(255,10,58,64)), ($s*0.012)
    for ($i = 0; $i -lt 8; $i++) {
        $a = (-90.0 + $i*$seg) * [Math]::PI / 180.0
        $x1 = $c + [Math]::Cos($a) * $innerR
        $y1 = $c + [Math]::Sin($a) * $innerR
        $x2 = $c + [Math]::Cos($a) * $outerR
        $y2 = $c + [Math]::Sin($a) * $outerR
        $g.DrawLine($sep, [float]$x1, [float]$y1, [float]$x2, [float]$y2)
    }
    $sep.Dispose()

    # --- outer gold ring ---
    $ringPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(255,255,224,130)), ($s*0.03)
    $g.DrawEllipse($ringPen, [float]($c - $outerR), [float]($c - $outerR), [float]($outerR*2), [float]($outerR*2))
    $ringPen.Dispose()

    # --- letter tiles around the wheel ---
    $tileR = $c * 0.56
    $tile = $s * 0.055
    for ($i = 0; $i -lt 8; $i++) {
        $a = (-90.0 + $i*$seg + $seg/2.0) * [Math]::PI / 180.0
        $cx = $c + [Math]::Cos($a) * $tileR
        $cy = $c + [Math]::Sin($a) * $tileR
        $tb = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(255,255,248,225))
        $g.FillRectangle($tb, [float]($cx - $tile/2), [float]($cy - $tile/2), [float]$tile, [float]$tile)
        $tb.Dispose()
        $tPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(255,10,58,64)), ($s*0.007)
        $g.DrawRectangle($tPen, [float]($cx - $tile/2), [float]($cy - $tile/2), [float]$tile, [float]$tile)
        $tPen.Dispose()
    }

    # --- center hub with Persian glyph "چ" ---
    $hubR = $c * 0.17
    $hubRect = New-Object System.Drawing.RectangleF ([float]($c - $hubR)), ([float]($c - $hubR)), ([float]($hubR*2)), ([float]($hubR*2))
    $hubBrush = New-Object System.Drawing.Drawing2D.LinearGradientBrush $hubRect, ([System.Drawing.Color]::FromArgb(255,255,236,179)), ([System.Drawing.Color]::FromArgb(255,245,158,11)), 90
    $g.FillEllipse($hubBrush, [float]($c - $hubR), [float]($c - $hubR), [float]($hubR*2), [float]($hubR*2))
    $hubBrush.Dispose()
    $hubPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(255,10,58,64)), ($s*0.015)
    $g.DrawEllipse($hubPen, [float]($c - $hubR), [float]($c - $hubR), [float]($hubR*2), [float]($hubR*2))
    $hubPen.Dispose()

    # glyph "چ"
    $fontSize = $hubR * 1.15
    $font = New-Object System.Drawing.Font "Tahoma", ($fontSize), ([System.Drawing.FontStyle]::Bold), ([System.Drawing.GraphicsUnit]::Pixel)
    $fmt = New-Object System.Drawing.StringFormat
    $fmt.Alignment = [System.Drawing.StringAlignment]::Center
    $fmt.LineAlignment = [System.Drawing.StringAlignment]::Center
    $txtBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(255,10,58,64))
    $textRect = New-Object System.Drawing.RectangleF ([float]($c - $hubR)), ([float]($c - $hubR)), ([float]($hubR*2)), ([float]($hubR*2))
    $g.DrawString("چ", $font, $txtBrush, $textRect, $fmt)
    $txtBrush.Dispose(); $font.Dispose(); $fmt.Dispose()

    $g.Dispose()
    return $bmp
}

function Save-Sized($bmp, $dstPath, $w) {
    $small = New-Object System.Drawing.Bitmap $w, $w
    $g2 = [System.Drawing.Graphics]::FromImage($small)
    $g2.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::AntiAlias
    $g2.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $d = New-Object System.Drawing.Rectangle 0,0,$w,$w
    $s = New-Object System.Drawing.Rectangle 0,0,$bmp.Width,$bmp.Height
    $g2.DrawImage($bmp, $d, $s, [System.Drawing.GraphicsUnit]::Pixel)
    $g2.Dispose()
    $small.Save($dstPath, [System.Drawing.Imaging.ImageFormat]::Png)
    $small.Dispose()
}

$base = New-WordWheelIcon 1024

# store 512
New-Item -ItemType Directory -Force store-assets | Out-Null
Save-Sized $base "store-assets\ic_launcher_store_512.png" 512

# density pngs
$densities = @{
  "mipmap-mdpi"    = 48
  "mipmap-hdpi"    = 72
  "mipmap-xhdpi"   = 96
  "mipmap-xxhdpi"  = 144
  "mipmap-xxxhdpi" = 192
}
foreach ($k in $densities.Keys) {
    $dir = "app\src\main\res\$k"
    Save-Sized $base "$dir\ic_launcher.png" $densities[$k]
    Save-Sized $base "$dir\ic_launcher_round.png" $densities[$k]
}

# adaptive full-canvas bitmap
$adaptiveDir = "app\src\main\res\mipmap-anydpi-v26"
Save-Sized $base "$adaptiveDir\ic_launcher_adaptive.png" 512

$base.Dispose()
Write-Host "Icons generated."
