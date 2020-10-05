@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  Method Count Starter
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal enabledelayedexpansion

set SDK=
set TOOL=

for /F "delims== tokens=2" %%i IN ('findstr /i "sdk.dir" local.properties') DO SET SDK=%%i
for /F "delims=' tokens=2" %%i IN ('findstr /i "buildToolsVersion" %~dp0app\build.gradle') DO SET TOOL=%%i

echo TOOL=%TOOL%
echo SDK=%SDK%

if "%SDK%" == "" (
    echo û�ҵ� Android Sdk ·�����ű���ֹ��
    goto end
)
if "%TOOL%" == "" (
    echo û�ҵ� Android Build Tool �汾���ű���ֹ��
    goto end
)

set SDK=%SDK:\\=\%
set SDK=%SDK:\:=:%

set MODULE=refresh-layout-kernel
set OUTPUT=methods-apk.txt

set PATH_TOOL=%SDK%\build-tools\%TOOL%\dx.bat
set PATH_JAR=%MODULE%/build/intermediates/intermediate-jars/debug/classes.jar
set PATH_DEX=%PATH_JAR%.dex

echo ���ڹ���������

call ./gradlew assemble
if "%ERRORLEVEL%" == "0" (
    echo.
    echo �����ɹ�������ʼ����DEX�ļ�������
) else (
    echo.
    echo ����ʧ�ܣ��ű���ֹ��
    goto end
)

:dex

echo ��������DEX������

call %PATH_TOOL% --dex --verbose --no-strict --output=%PATH_DEX% %PATH_JAR% >NUL
if "%ERRORLEVEL%" == "0" (
    echo.
    echo ����DEX�ļ��ɹ�������ʼͳ�ơ�����
) else (
    echo.
    echo ����DEXʧ�ܣ��ű���ֹ��
    goto end
)

call java -jar ./art/dex-method-counts.jar --filter=all --include-classes --include-detail --output-style=tree   %PATH_DEX% > %OUTPUT%

call ./method-count-words.bat %OUTPUT% %OUTPUT:.txt=-count.txt%

:end

if "%OS%"=="Windows_NT" endlocal