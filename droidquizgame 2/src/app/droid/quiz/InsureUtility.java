package app.droid.quiz;

import java.io.FileNotFoundException;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;


public class InsureUtility extends Activity{
	
	public static String Username = "",Password = "",imei,Remember = "N",ProfileImg = "", fname = "", lname = "", mname = "";
	
	public static boolean skylogout = false;
	
	public static String deviceID;
	
	public static String Today;

	private static Bitmap bitmap;
	
	public static String getTodayDate(){
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int date = calendar.get(Calendar.DATE);
		Today = year+"-"+month+"-"+date;
		return Today;
		
	}
	
	public static String getimei(Context c){ 
	
		TelephonyManager telephonyManager = (TelephonyManager)c.getSystemService(Context.TELEPHONY_SERVICE);
        deviceID =telephonyManager.getDeviceId();
        Log.i("imei",""+deviceID);
        return deviceID;
	}

	
	public static boolean isInternetAvailable(Context context) {
		ConnectivityManager connectionManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectionManager.getActiveNetworkInfo() != null && connectionManager.getActiveNetworkInfo().isAvailable()) {
			NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
			if(networkInfo.getTypeName().equals("MOBILE")){
				
			}else{
				
			}
			return true;
		}else
		return false;
	
	}
	
	
	
	public static void getUsernamePasswordFromPreferenes(Context c)
	{
		SharedPreferences manger =PreferenceManager.getDefaultSharedPreferences(c);
		Username = manger.getString(c.getString(R.string.username_key), "");
		Password = manger.getString(c.getString(R.string.password_key), "");
		Remember = manger.getString(c.getString(R.string.rem_key), "");
		ProfileImg = manger.getString(c.getString(R.string.profileImg_key), "");
		fname =manger.getString(c.getString(R.string.fname_key), "");
		lname =manger.getString(c.getString(R.string.lname_key), "");
//		mname =manger.getString(c.getString(R.string.mname_key), "");
		
	}
	
//	public static void saveusercredentials(Context context,String updatedUserName,String updatedPassword) {
//		// TODO Auto-generated method stub
//		String username= context.getString(R.string.username_key);
//		String password = context.getString(R.string.password_key);
//		
//		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(username,updatedUserName).commit();
//		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(password,updatedPassword).commit();
//		
//	}
	
	public static void saveusercredentials(Context context,String updatedUserName,String updatedPassword, String updatedrem, String updatedimg) {
		// TODO Auto-generated method stub
		String username= context.getString(R.string.username_key);
		String password = context.getString(R.string.password_key);
		String rem_chk = context.getString(R.string.rem_key);
		String img_uri = context.getString(R.string.profileImg_key);
		
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(username,updatedUserName).commit();
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(password,updatedPassword).commit();
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(rem_chk,updatedrem).commit();
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(img_uri,updatedimg).commit();
		
	}
	
	public static void saveuserlogincredentials(Context context,String updatedUserName,String updatedPassword, String updatedrem) {
		// TODO Auto-generated method stub
		String username= context.getString(R.string.username_key);
		String password = context.getString(R.string.password_key);
		String rem_chk = context.getString(R.string.rem_key);
		
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(username,updatedUserName).commit();
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(password,updatedPassword).commit();
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(rem_chk,updatedrem).commit();
		
	}
	
	public static void saveusernames(Context context,String updatedFirstName,String updatedLastName, String updatedMiddleName) {
		// TODO Auto-generated method stub
	String	 fname= context.getString(R.string.fname_key);
	String	 lname = context.getString(R.string.lname_key);
	String 	mname = context.getString(R.string.mname_key);
		
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(fname,updatedFirstName).commit();
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(lname,updatedLastName).commit();
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(mname,updatedMiddleName).commit();
		
	}
	
	public static Bitmap getUserPhoto(Context c){
	
		SharedPreferences manger =PreferenceManager.getDefaultSharedPreferences(c);
		ProfileImg = manger.getString(c.getString(R.string.profileImg_key), "");
		Uri pic = Uri.parse(ProfileImg);
		try {
			bitmap = BitmapFactory.decodeStream(c.getContentResolver().openInputStream(pic));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bitmap;
		
	}
	
	public static String FormatDate(String Date){
		
		if(Date.contains("/")){
			
			Date = Date.replace("/", "-").trim();
		}
		
		return Date;
	}
	
	public static String [] PlanType = {"none","platinum","gold","bronze"};
	
	public static String [] Travel_PlanType = {"none","Schengen","Other European","World-wide Basic","World-wide PLus","Haji/umra","African Plan","Student Plan"};
	
	public static String [] Travel_dest = {"none","schengen","World-wide","Other Europe","Student"};
	
	public static String [] countryAbbList= {
		"USA",
		"AFG", 
		"ALB", 
		"DZA", 
		"ASM", 
		"AND", 
		"AGO", 
		"AIA", 
		"ATA", 
		"ATG", 
		"ARG", 
		"ARM", 
		"ABW", 
		"AUS", 
		"AUT", 
		"AZE", 
		"BHS", 
		"BHR", 
		"BGD", 
		"BRB", 
		"BLR", 
		"BEL", 
		"BLZ", 
		"BEN", 
		"BMU", 
		"BTN", 
		"BOL", 
		"BIH", 
		"BWA", 
		"BRA", 
		"IOT", 
		"BRN", 
		"BGR", 
		"BFA", 
		"BDI", 
		"KHM", 
		"CMR", 
		"CAN", 
		"CPV", 
		"CYM", 
		"CAF", 
		"TCD", 
		"CHL", 
		"CHN", 
		"CXR", 
		"CCK", 
		"COL", 
		"COM", 
		"COG", 
		"COD", 
		"COK", 
		"CRI", 
		"HRV", 
		"CUB", 
		"CYP", 
		"CZE", 
		"DNK", 
		"DJI", 
		"DMA", 
		"DOM", 
		"ECU", 
		"EGY", 
		"SLV", 
		"GNQ", 
		"ERI", 
		"EST", 
		"ETH", 
		"FLK", 
		"FRO", 
		"FJI", 
		"FIN", 
		"FRA", 
		"GUF", 
		"PYF", 
		"ATF", 
		"GAB", 
		"GMB", 
		"GEO", 
		"DEU", 
		"GHA", 
		"GIB", 
		"GRC", 
		"GRL", 
		"GRD", 
		"GLP", 
		"GUM", 
		"GTM", 
		"GIN", 
		"GNB", 
		"GUY", 
		"HTI", 
		"HND", 
		"HKG", 
		"HUN", 
		"ISL", 
		"IND", 
		"IDN", 
		"IRN", 
		"IRQ", 
		"IRL", 
		"ISR", 
		"ITA", 
		"JAM", 
		"JPN", 
		"JOR", 
		"KAZ", 
		"KEN", 
		"KIR", 
		"PRK", 
		"KOR", 
		"KWT", 
		"KGZ", 
		"LAO", 
		"LVA", 
		"LBN", 
		"LSO", 
		"LBR", 
		"LBY", 
		"LIE", 
		"LTU", 
		"LUX", 
		"MAC", 
		"MKD", 
		"MDG", 
		"MWI", 
		"MYS", 
		"MDV", 
		"MLI", 
		"MLT", 
		"MHL", 
		"MTQ", 
		"MRT", 
		"MUS", 
		"MYT", 
		"MEX", 
		"FSM", 
		"MDA", 
		"MCO", 
		"MNG", 
		"MSR", 
		"MAR", 
		"MOZ", 
		"MMR", 
		"NAM", 
		"NRU", 
		"NPL", 
		"NLD", 
		"ANT", 
		"NCL", 
		"NZL", 
		"NIC", 
		"NER", 
		"NGA", 
		"NIU", 
		"NFK", 
		"MNP", 
		"NOR", 
		"OMN", 
		"PAK", 
		"PLW", 
		"PSE", 
		"PAN", 
		"PNG", 
		"PRY", 
		"PER", 
		"PHL", 
		"POL", 
		"PRT", 
		"PRI", 
		"QAT", 
		"REU", 
		"ROU", 
		"RUS", 
		"RWA", 
		"SHN", 
		"KNA", 
		"LCA", 
		"SPM", 
		"VCT", 
		"WSM", 
		"SMR", 
		"STP", 
		"SAU", 
		"SEN", 
		"SYC", 
		"SLE", 
		"SGP", 
		"SVK", 
		"SVN", 
		"SLB", 
		"SOM", 
		"ZAF", 
		"ESP", 
		"LKA", 
		"SDN", 
		"SUR", 
		"SWZ", 
		"SWE", 
		"CHE", 
		"SYR", 
		"TWN", 
		"TJK", 
		"TZA", 
		"THA", 
		"TLS", 
		"TGO", 
		"TKL", 
		"TON", 
		"TTO", 
		"TUN", 
		"TUR", 
		"TKM", 
		"TCA", 
		"TUV", 
		"UGA", 
		"UKR", 
		"ARE", 
		"GBR", 
		"USA", 
		"URY", 
		"UZB", 
		"VUT", 
		"VEN", 
		"VNM", 
		"VGB", 
		"VIR", 
		"WLF", 
		"YEM", 
		"ZMB", 
		"ZWE", 
		"CIV", 
		"ALA", 
		"GGY", 
		"IMN", 
		"JEY", 
		"MNE", 
		"SRB"
		};
		public static String[] countryCodeList = {
			"1",
			"93",
			"355",
			"213",
			"684",
			"376",
			"244",
			"1264",
			"672",
			"1268",
			"54",
			"374",
			"297",
			"61",
			"43",
			"994",
			"1242",
			"973",
			"880",
			"1246",
			"375",
			"32",
			"501",
			"229",
			"1441",
			"975",
			"591",
			"387",
			"267",
			"55",
			"1284",
			"673",
			"359",
			"226",
			"257",
			"855",
			"237",
			"1",
			"238",
			"1345",
			"236",
			"235",
			"56",
			"86",
			"618",
			"61",
			"57",
			"269",
			"242",
			"243",
			"682",
			"506",
			"385",
			"53",
			"357",
			"420",
			"45",
			"253",
			"1767",
			"1809",
			"593",
			"20",
			"503",
			"240",
			"291",
			"372",
			"251",
			"500",
			"298",
			"679",
			"358",
			"33",
			"596",
			"594",
			"689",
			"241",
			"220",
			"995",
			"49",
			"233",
			"350",
			"30",
			"299",
			"1473",
			"590",
			"1671",
			"502",
			"224",
			"245",
			"592",
			"509",
			"504",
			"852",
			"36",
			"354",
			"91",
			"62",
			"98",
			"964",
			"353",
			"972",
			"39",
			"1876",
			"81",
			"962",
			"7",
			"254",
			"686",
			"850",
			"82",
			"965",
			"996",
			"856",
			"371",
			"961",
			"266",
			"231",
			"218",
			"423",
			"370",
			"352",
			"853",
			"389",
			"261",
			"265",
			"60",
			"960",
			"223",
			"356",
			"692",
			"596",
			"222",
			"230",
			"269",
			"52",
			"691",
			"1808",
			"377",
			"976",
			"1664",
			"212",
			"258",
			"95",
			"264",
			"674",
			"977",
			"31",
			"599",
			"687",
			"64",
			"505",
			"227",
			"234",
			"683",
			"672",
			"1670",
			"47",
			"968",
			"92",
			"680",
			"970",
			"507",
			"675",
			"595",
			"51",
			"63",
			"48",
			"351",
			"1787",
			"974",
			"262",
			"40",
			"7",
			"250",
			"290",
			"1869",
			"1758",
			"508",
			"1784",
			"685",
			"378",
			"239",
			"966",
			"221",
			"248",
			"232",
			"65",
			"421",
			"386",
			"677",
			"252",
			"27",
			"34",
			"94",
			"249",
			"597",
			"268",
			"46",
			"41",
			"963",
			"886",
			"992",
			"255",
			"66",
			"670",
			"228",
			"690",
			"676",
			"1868",
			"216",
			"90",
			"993",
			"1649",
			"688",
			"256",
			"380",
			"971",
			"44",
			"1",
			"598",
			"998",
			"678",
			"58",
			"84",
			"1284",
			"808",
			"681",
			"967",
			"260",
			"263",
			"225",
			"35818",
			"441481",
			"441624",
			"441534",
			"382",
			"381"
		};
}

