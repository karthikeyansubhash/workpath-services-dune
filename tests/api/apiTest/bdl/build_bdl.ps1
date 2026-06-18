# 빌드 실패 시 바로 중단
$ErrorActionPreference = "Stop"

# 현재 위치 저장
$OriginalLocation = Get-Location

# ==== 설정 부분 ====
$SPU_DIR   = "D:\share\HPKTool\hpktool-dune-master\common\spu_windows"
$WORK_ROOT = (Get-Location).ProviderPath
$OUT_ROOT  = Join-Path $WORK_ROOT "newoutput"
$STAGE_DIR = Join-Path $OUT_ROOT "stage"

$SQUASH_DIR = Join-Path $OUT_ROOT "squash"
$TAR_DIR    = Join-Path $OUT_ROOT "tar"

$DESCRIPTION = "Workpath APITest"
$NAME        = "Workpath APITest"
$REVISION    = "20251209"
$VENDOR      = "HP"
$PAK_ID      = "b081727d-bb2e-46f6-94c0-5f17c4587b16"

$PAK_PATH    = Join-Path $OUT_ROOT "solution.pak"
$BDL_PATH    = Join-Path $OUT_ROOT "solution.bdl"
$SQUASH_PATH = Join-Path $SQUASH_DIR "solution.squash"
$TAR_PATH    = Join-Path $TAR_DIR "solution.tar.gz"

# ==== 준비 작업 ====
Write-Host "[INFO] Changing to SPU directory: $SPU_DIR"
Set-Location $SPU_DIR

Write-Host "[INFO] Creating output directories..."
New-Item -ItemType Directory -Force -Path $SQUASH_DIR | Out-Null
New-Item -ItemType Directory -Force -Path $TAR_DIR    | Out-Null

# Windows 경로를 /mnt/d/... 형식으로 바꾸는 헬퍼 함수
function Convert-ToMntPath([string] $winPath) {
    if (-not $winPath) { return $winPath }
    # 예: D:\share\sample -> /mnt/d/share/sample
    $drive = $winPath.Substring(0,1).ToLower()
    $rest  = $winPath.Substring(2) -replace '\\','/'
    return "/mnt/$drive$rest"
}

# ==== 1. squashfs 생성 ====
Write-Host "[INFO] Running squashfs..."
$stageMnt  = Convert-ToMntPath $STAGE_DIR
$squashMnt = Convert-ToMntPath $SQUASH_PATH
.\spu-cli.exe squashfs `
  --input  $stageMnt `
  --output $squashMnt

# ==== 2. tar.gz 생성 ====
Write-Host "[INFO] Creating tar.gz..."
$squashDirMnt = Convert-ToMntPath $SQUASH_DIR
$tarMnt       = Convert-ToMntPath $TAR_PATH
.\spu-cli.exe tar `
  --input  $squashDirMnt `
  --output $tarMnt

# ==== 3. pak 생성 ====
Write-Host "[INFO] Creating .pak..."
.\spu-cli.exe fimpak `
  --description $DESCRIPTION `
  --folder      $TAR_DIR `
  --name        $NAME `
  --output      $PAK_PATH `
  --revision    $REVISION `
  --vendor      $VENDOR

# ==== 4. bdl 생성 ====
Write-Host "[INFO] Creating .bdl..."
.\spu-cli.exe fimbdl `
  --description $DESCRIPTION `
  --input       $PAK_PATH `
  --id          $PAK_ID `
  --name        $NAME `
  --output      $BDL_PATH `
  --revision    $REVISION `
  --type        "unverified" `
  --vendor      $VENDOR

Write-Host "[INFO] Done. BDL created at: $BDL_PATH"

# 원래 위치로 복귀
Set-Location $OriginalLocation