package com.marklogic.contentpump;

import junit.framework.TestCase;

import com.marklogic.contentpump.utilities.OptionsFileUtil;
import com.marklogic.xcc.ResultSequence;

public class TestDistributedImportDocs extends TestCase {
    public TestDistributedImportDocs(String name) {
        super(name);
    }
    
    public void testImportMixedDocs() throws Exception {
        String cmd = "IMPORT -password admin -username admin -host localhost"
            + " -input_file_path " + Constants.TEST_PATH.toUri() + "/wiki"
            + " -port 5275 -output_uri_prefix ABC"
            + " -output_collections test,ML" 
            + " -hadoop_conf_dir " + Constants.HADOOP_CONF_DIR;
        String[] args = cmd.split(" ");
        assertFalse(args.length == 0);

        Utils.clearDB("xcc://admin:admin@localhost:5275", "Documents");

        String[] expandedArgs = null;
        expandedArgs = OptionsFileUtil.expandArguments(args);
        Utils.prepareDistributedMode();
        ContentPump.runCommand(expandedArgs);

        ResultSequence result = Utils.runQuery(
            "xcc://admin:admin@localhost:5275",
            "fn:count(fn:collection(\"ML\"))");
        assertTrue(result.hasNext());
        assertEquals("93", result.next().asString());
    }
    
    public void testImportText() throws Exception {
        String cmd = "IMPORT -password admin -username admin -host localhost"
            + " -input_file_path " + Constants.TEST_PATH.toUri() + "/wiki/AbacuS"
            + " -port 5275 -output_uri_prefix ABC"
            + " -output_collections test,ML -document_type text"
            + " -hadoop_conf_dir " + Constants.HADOOP_CONF_DIR;
        String[] args = cmd.split(" ");
        assertFalse(args.length == 0);

        Utils.clearDB("xcc://admin:admin@localhost:5275", "Documents");

        String[] expandedArgs = null;
        expandedArgs = OptionsFileUtil.expandArguments(args);
        Utils.prepareDistributedMode();
        ContentPump.runCommand(expandedArgs);

        ResultSequence result = Utils.runQuery(
            "xcc://admin:admin@localhost:5275", "fn:count(fn:collection())");
        assertTrue(result.hasNext());
        assertEquals("1", result.next().asString());
    }
 
}
