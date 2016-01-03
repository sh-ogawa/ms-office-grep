ExcelGrep
============

##1.概要
MS-Office（ExcelとWord）の検索ツール。

指定したフォルダからファイルを再帰的に検索し、  
特定の文字列を検索してコンソールに出力します。  
※あいまい検索、完全一致検索が可能です。  

##2.使い方
####(1)jarファイル作成
ソースをチェックアウトし、  
mvn install  
コマンドでjarファイルを作成してください。

####(2)バッチファイル作成
適用な.batファイルを作成し、  
java -jar excelgrep-0.0.1-SNAPSHOT-jar-with-dependencies ${検索対象ディレクトリ} ${検索対象文字列} ${検索モード}
pause  
と記載し、同ディレクトリにjarファイルを配置してください。  
検索モードに「FUZZY」を指定した場合はあいまい検索を、  
検索モードに「STRICTLY」を指定した場合は完全一致検索を行います。  
置換文字列を指定した場合は文字列のgrep置換を実行します。
※WordはFUZZYのみです。

####(3)バッチファイルを実行
バッチファイルを実行すると、検索が始まり、grep結果をカレントディレクトリにファイル出力します。  