package junwei.bswabe;

import com.junwei.bswabe.Bswabe;
import com.junwei.bswabe.BswabeCph;
import com.junwei.bswabe.BswabeCphKey;
import com.junwei.bswabe.BswabeElementBoolean;
import com.junwei.bswabe.BswabeMsk;
import com.junwei.bswabe.BswabePrv;
import com.junwei.bswabe.BswabePub;
import com.mitu.utils.exceptions.AttributesNotSatisfiedException;
import com.mitu.utils.exceptions.MalformedPolicyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class Demo {
	final static boolean DEBUG = true;

	final static String dir = "src/test/java/";

	final static String inputfile = dir + "RealHuman.png";
	final static String encfile = dir + "RealHuman.png.cpabe";
	final static String decfile = dir + "RealHuman.png.new";

	static final String[] attr_kevin = {
			"business_staff",
			"executive_level_flexint_0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_7",
			"executive_level_flexint_x0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xx",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1x",
			"executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1",
			"executive_level_ge_2^02",
			"executive_level_lt_2^04",
			"executive_level_lt_2^08",
			"executive_level_lt_2^16",
			"executive_level_lt_2^32",
			"hire_date_flexint_0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_1332945715",
			"hire_date_flexint_x0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1x",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1",
			"hire_date_ge_2^02",
			"hire_date_ge_2^04",
			"hire_date_ge_2^08",
			"hire_date_ge_2^16",
			"hire_date_lt_2^32",
			"office_flexint_0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_2362",
			"office_flexint_x0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1x",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0",
			"office_ge_2^02", "office_ge_2^04", "office_ge_2^08",
			"office_lt_2^16", "office_lt_2^32", "strategy_team" };
	static final String[] attr_sara = {
			"hire_date_flexint_0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_1332980893",
			"hire_date_flexint_x0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xx",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0x",
			"hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1",
			"hire_date_ge_2^02",
			"hire_date_ge_2^04",
			"hire_date_ge_2^08",
			"hire_date_ge_2^16",
			"hire_date_lt_2^32",
			"it_department",
			"office_flexint_0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_1431",
			"office_flexint_x0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xx",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1x",
			"office_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1",
			"office_ge_2^02", "office_ge_2^04", "office_ge_2^08",
			"office_lt_2^16", "office_lt_2^32", "sysadmin" };
	static final String policy_kevin_or_sara = "hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxx hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxx 2of2 hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxx hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxx hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxx 1of4 hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxx hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxx 3of3 hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxx 1of2 hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxx hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxx hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxx 4of4 hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxx hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxx 1of3 hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxx 2of2 hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxx hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxx 1of3 hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxx 2of2 hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxx hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxx 1of3 hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxx hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxx hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxx hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxx 5of5 hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxx hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxx hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxx 1of4 hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx hire_date_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx hire_date_lt_2^32 4of4 security_team 1of2 sysadmin 2of2 executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1x executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1 1of2 executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xx 2of2 executive_level_flexint_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1xxx 1of2 executive_level_ge_2^04 executive_level_ge_2^08 executive_level_ge_2^16 executive_level_ge_2^32 1of5 audit_group strategy_team 2of3 business_staff 2of2 1of2";

	static final String testPolicy = "foo bar fim 2of3 baf 1of2";
	static BswabePub pub;
	static BswabeMsk msk;

	private static String[] mergeArray(String[] array1, String[] array2){
		var result = new String[array1.length + array2.length];  
		System.arraycopy(array1, 0, result, 0, array1.length);  
		System.arraycopy(array2, 0, result, array1.length, array2.length);  
		return result;
	}

	@BeforeEach
	public void setUp() {
		Map<String, String> defaultMap = new HashMap<>() {{
			put("type", "a");
			put("q", "6369360761975870121762638569043728784537618524939795659581725459764519487632130011828250189407458902559556082799450871019292319559501351774206580668498539");
			put("h", "8716186967272981946338881743674542758348861153366380201939968258588498384514697695543719672616341557740140");
			put("r", "730750818665451459101842416358141509827966402561");
			put("exp2", "159");
			put("exp1", "17");
			put("sign1", "1");
			put("sign0", "1");
		}};
	    pub = new BswabePub();
		msk = new BswabeMsk();
		println("//demo for co.junwei.bswabe: start to setup");
		Bswabe.setup(pub, msk, defaultMap);
		println("//demo for co.junwei.bswabe: end to setup");

	}

	@TestFactory
	public Stream<DynamicTest> testBswabeWithPolicesAndAttributes() {
	    var policies = new String[]{
	    	testPolicy,
	    	testPolicy,
	    	policy_kevin_or_sara,
	    	policy_kevin_or_sara,
	    	"doctor windsor 2of2 heart_surgeon emergency doctor 3of3 1of2",
	    	"doctor emergency 2of2 heart_surgeon emergency 2of2 1of2",
	    	"doctor emergency windsor 3of3",
	    	"doctor windsor heart_surgeon 3of3"
	    };
	    String[] testSystem = { "baf", "fim1", "fim", "foo" };
	    String[] saraAndKevinAttr = mergeArray(attr_sara, attr_kevin);
	    String[] doctorSystem = {"doctor", "windsor", "detroit", "heart_surgeon", "emergency"};
	    String[] doctorAttributes = "doctor detroit heart_surgeon emergency".split(" ");
	    // all possible attributes used in keygen
	    var attrs = new String[][]{
	    	testSystem,
	    	testSystem,
	    	saraAndKevinAttr,
	    	saraAndKevinAttr,
	    	doctorSystem,
	    	doctorSystem,
	    	doctorSystem,
	    	doctorSystem
	    };
	    var userAttributes = new String[][]{
	    	/* First one is supposed to fail*/
	    	{"fim"},
	    	{"fim", "foo"},
	    	attr_sara,
	    	attr_kevin,
	    	doctorAttributes,
	    	doctorAttributes,
	    	doctorAttributes,
	    	doctorAttributes
	    };

	    var passes = new boolean[]{
	    	false,
	    	true,
	    	/*Currently sara is not passing - not sure why - it might be correct and the variable might just be missed name */
	    	true,
	    	true,
	    	true,
	    	true,
	    	false,
	    	false
	    };
	    int testCases = policies.length;
	    return IntStream.range(0, testCases)
	      .mapToObj(testCase -> dynamicTest("testCase=" + testCase,
	            () -> BswabeTest(policies[testCase], attrs[testCase], userAttributes[testCase], passes[testCase]))
	      );
	}
	
	public static void BswabeTest(String policy, String[] attr, String[] user_attributes, boolean areEqual) throws NoSuchAlgorithmException, MalformedPolicyException, AttributesNotSatisfiedException {
		BswabePrv prv, prv_delegate;
		BswabeCph cph;
		BswabeElementBoolean result;

		
		println("\n//demo for co.junwei.bswabe: start to keygen");
		prv = Bswabe.keygen(pub, msk, attr);
		println("//demo for co.junwei.bswabe: end to keygen");

		println("\n//demo for co.junwei.bswabe: start to delegate");
		prv_delegate = Bswabe.delegate(pub, prv, user_attributes);
		println("//demo for co.junwei.bswabe: end to delegate");

		println("\n//demo for co.junwei.bswabe: start to encrypt");
		BswabeCphKey crypted = Bswabe.encrypt(pub, policy);
		cph = crypted.cph;
		println("//demo for co.junwei.bswabe: end to encrypt");

		println("\n//demo for co.junwei.bswabe: start to decrypt");
		result = Bswabe.decrypt(pub, prv, cph);
		println("//demo for co.junwei.bswabe: end to decrypt");
		assertTrue((result.b) && (result.e.equals(crypted.key)));
		try{
			println("\n//demo for co.junwei.bswabe: start to decrypt with delegated key");
			result = Bswabe.decrypt(pub, prv_delegate, cph);
			println("//demo for co.junwei.bswabe: end to decrypt with delegated key");
			assertEquals((result.b) && (result.e.equals(crypted.key)),  areEqual);
		} catch (AttributesNotSatisfiedException e) {
            /* The policy was supposed to fail the user */
            assertFalse(areEqual);
        }
	}

	private static void println(Object o) {
		if (DEBUG)
			System.out.println(o);
	}
}
