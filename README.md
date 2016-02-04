ms-office-grep
============

##1.概要
MS-Office（ExcelとWord）の検索ツール。

指定したフォルダからファイルを再帰的に検索し、  
特定の文字列を検索して外部ファイルに出力します。  

結果にはExcelのHyperlink関数をセットしているため、
結果ファイルをexcelで開けばセルをクリックすることで直接対象ファイルのセルにジャンプできます。
（Wordの場合はファイルを開くのみです）

##2.使い方
####(1)jarファイル作成
ソースをチェックアウトし、  
mvn install  
コマンドで実行可能jarファイルを作成してください。

excelgrep-0.0.1-SNAPSHOT-jar-with-dependencies
が生成されます。

####(2)実行可能jarファイルを実行
excelgrep-0.0.1-SNAPSHOT-jar-with-dependenciesを実行すると検索ウィンドウが表示するので
必要な情報を入力して実行してください。
