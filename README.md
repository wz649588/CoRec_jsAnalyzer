# CoRec_jsAnalyzer

This project is provided to investigate and recommend co-changed entities for JavaScript programs.

#Requirements:

1. Eclipse Luna standard 4.4, jdk 1.8.

2. Sqlite3.

3. Node.js 10.15.

4. Additional for TypedAST: Python 3.6+, pip3, Docker engine.

# Git commit mining

1. Download the projects to your project repo folder.

2. change line 334 in gittool.GitMinerVersion.java to the project repo folder.

3. For the project (Meteor for example) without package.json file, uncomment line 243-294 in gittool.GitMinerVersion.java to generate package.json files.

# Generate AST.json files using TypedAST

1. Read the document https://buildmedia.readthedocs.org/media/pdf/r2c/latest/r2c.pdf to learn how to get access to TypedAST, and generate AST.json files for the changed JavaScript files you have mined in the previous step (Git commit mining).

2. Run ast-consumer to extract AST.json archive.

3. Run vt.edu.util.UnZipTgz to unzip tgz files.

# CDG extracting and characterization of co-changed entities

1. Change line 35 in jstest.ChangeExtracterClient3.java to the folder where the AST.json files are stored.

2. Change line 27 in jstest.TestChange2.java to the project repo folder.

3. Change line 12 in vt.edu.sql.SqliteManager.java to the sqlite file that is used.

4. Change line 20, 21 in jstest.jsToJson to where the "jsonModule.js" and "jsonScript.js" files are stored.

5. Run mainNode method in jstest.TestChange2.java to extract CDGs for those projects (Node.js) without AST.json files. Run mainOthers method in jstest.TestChange2 to extract CDGs for those projects with AST.json files.

6. Run vt.edu.empirical.GraphStatExtraction to characterize the co-changed entities.

# Co-change pattern Extraction

Change the project String Array in vt.edu.graph.LMatchPatternExtraction.java, vt.edu.graph.LMatchCollapser.java, vt.edu.graph.LMatchPicGenerator.java, vt.edu.graph.LMatchSummary.java, vt.edu.graph.LMatchTopPattern.java. Run the main method in those java files in sequence.

# Build data for machine learning

1. For the projects without AST.json files (Node.js), run mainNodeData method in jstest.TestChange2.java.

2. For other projects, run mianFromDataOthers method in jstest.TestChange2.java.

3. Convert the data file to arff files (use wekaPre.ARFFGenerator.java)

4. Change line 322 and line 323 in wekaPre.WekaTest.java to where the training and testing arffs are stored.

5. Many typeds of machine learning models can be used to train the data in wekaPre.WekaTest.java.

# Data link (get data from link below)

https://drive.google.com/drive/folders/1TDcIHvBMLJJ00jUzV_-SYwhlbc-CYMkp?usp=sharing
