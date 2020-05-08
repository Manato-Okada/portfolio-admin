package com.seattleacademy.team20;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Handles requests for the application home page.
 */
@Controller
public class SkillController {

	private static final Logger logger = LoggerFactory.getLogger(SkillController.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String skillUpload(Locale locale, Model model) throws IOException {
		logger.info("Welcome home! The client locale is {}.", locale);
		initialize();
		List<Skill> skills = selectSkills();
		uploadSkill(skills);

		return "skillUpload";
	}

	//ここからタスク10

	//	//Listの宣言
	public List<Skill> selectSkills() {
		final String sql = "select * from skills";
		return jdbcTemplate.query(sql, new RowMapper<Skill>() {
			public Skill mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Skill(rs.getString("category"),
						rs.getString("name"), rs.getInt("score"));

			}
		});
	}

	private FirebaseApp app;

	// public void FirebaseUploadService()throws IOException{
	//		this.initialize
	//	}

	//SDKの初期化
	public void initialize() throws IOException {
		FileInputStream refreshToken = new FileInputStream(
				"/Users/okadamanato/Downloads/deployportfolio-firebase-adminsdk-hlfyo-48160a376c.json");
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(refreshToken))
				.setDatabaseUrl("https://deployportfolio.firebaseio.com/")
				.build();
		app = FirebaseApp.initializeApp(options, "other");
	}

	public void uploadSkill(List<Skill> skills) {
		//データの保存
		final FirebaseDatabase database = FirebaseDatabase.getInstance(app);
		DatabaseReference ref = database.getReference("skills");

		//	データの取得
		//	データを取得してから整形する
		//	ReaitimeDatabaseにアップロードする
		//	JSPに渡すデータを設定する
		// DatabaseReference skillsref = ref.child("skills");

		//Map型のリストを作る。MapはStringで聞かれたものに対し、object型で返すようにしている。
		List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		Map<String, List<Skill>> skillMap = skills.stream().collect(Collectors.groupingBy(Skill::getCategory));
		for (Map.Entry<String, List<Skill>> entry : skillMap.entrySet()) {
			//for(Skill skill: skills) {
			map = new HashMap<>();
			//map.put("category",skill.getCategory());
			map.put("category", entry.getKey());
			map.put("skill", entry.getValue());
			datalist.add(map);
		}

		//			map.put("skills",skills.stream()
		//					.collect(Collectors.groupingBy(Skill::getCategory)));

		//}

		ref.setValue(datalist, new DatabaseReference.CompletionListener() {
			@Override
			public void onComplete(DatabaseError databaseError, DatabaseReference databasereference) {
				if (databaseError != null) {
					System.out.println("Data　could be saved" + databaseError.getMessage());
				} else {
					System.out.println("Data save successfully.");
				}
			}
		});
	}
}

//RealtimeDatabase用に整形してる
//		List<Skill> List = jdbcTemplate.query("select category, name, score from skills",
//				new RowMapper<Skill>(){
//					@SuppressWarnings({"rawtypes","unchecked"})
//					public Skill mapRow (ResultSet rs, int rowNum) throws SQLException{
////						Map<String,String> map = new HashMap();
////
////						map.put("category",rs.getString("category"));
////						map.put("name", rs.getString("name"));
////						map.put("score",rs.getString("score"));
//						return new Skill(rs.getString("category"),rs.getString("name"),rs.getInt("score"));
//					}
//				});
//		}
//}

//skillsRef.updateChildrenAsync(dataMap);
//	ref.setValue(dataList, new DatabaseReference.CompletionListener() {
//		@override
//		public void onComplete(DatabaseError databaseError,DatabaseReference databasereference) {
//			if(databaseError != null) {
//				System.out.println("Data　could be saved" + databaseError.getMessage());
//			}else {
//				System.out.println("Data save successfully.");
//			}
//		}
//	});
//	}
//}
////
