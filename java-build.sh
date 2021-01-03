#!/usr/bin/env sh

echo "------------------------------------------------"
echo "                  Java Build"
echo "------------------------------------------------"

echo "Build SMS"
( cd sms ; ./gradlew clean build --info )

echo "Build ThirdParty"
( cd third-party ; ./gradlew clean build --info )

echo "Build DataVoucher"
( cd data-voucher ; ./gradlew clean build --info )

echo "Build PurchaseData"
( cd purchase-data ; ./gradlew clean build --info )

echo "------------------------------------------------"
echo "                Build Success"
echo "------------------------------------------------"
