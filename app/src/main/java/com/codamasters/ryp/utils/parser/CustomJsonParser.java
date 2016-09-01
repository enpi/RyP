package com.codamasters.ryp.utils.parser;

import android.util.Log;

import com.codamasters.ryp.model.Degree;
import com.codamasters.ryp.model.Location;
import com.codamasters.ryp.model.Professor;
import com.codamasters.ryp.model.University;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Juan on 20/08/2016.
 */
public class CustomJsonParser {

    private JSONObject jsonObject;
    private ArrayList<Professor> professors;
    private ArrayList<Degree> degrees;
    private ArrayList<University> universities;

    private ArrayList<String> professorsKeys;
    private ArrayList<String> degreesKeys;
    private ArrayList<String> universitiesKeys;

    public CustomJsonParser(JSONObject jsonObject){
        this.jsonObject = jsonObject;

        universities = new ArrayList<>();
        degrees = new ArrayList<>();
        professors = new ArrayList<>();

        professorsKeys = new ArrayList<>();
        degreesKeys = new ArrayList<>();
        universitiesKeys = new ArrayList<>();

    }

    public void parse() throws JSONException {
        JSONObject hitsObj = jsonObject.getJSONObject("hits");
        Log.d("HITS", hitsObj.toString());

        JSONArray hitsArr = hitsObj.getJSONArray("hits");
        Log.d("HITS 2", hitsArr.toString());

        for (int i = 0, size = hitsArr.length(); i < size; i++){
            JSONObject object = hitsArr.getJSONObject(i);

            String type = object.getString("_type");
            JSONObject content = object.getJSONObject("_source");

            switch (type){
                case "university":
                    universitiesKeys.add(object.getString("_id"));
                    addUniversity(content);
                    break;
                case "degree":
                    degreesKeys.add(object.getString("_id"));
                    addDegree(content);
                    break;
                case "professor":
                    professorsKeys.add(object.getString("_id"));
                    addProfessor(content);
                    break;
            }
        }

        sortArraysByElo();
    }

    private void addUniversity(JSONObject object) throws JSONException {
        University university = new University();
        university.setName(object.getString("name"));
        university.setElo(object.getDouble("elo"));
        university.setSumRating(object.getDouble("sumRating"));
        university.setNumVotes(object.getInt("numVotes"));

        universities.add(university);

        Log.d("UNIVERSITY", "ADDED");
    }

    private void addDegree(JSONObject object) throws JSONException {


        Degree degree = new Degree();
        degree.setName(object.getString("name"));
        degree.setElo(object.getDouble("elo"));
        degree.setSumRating(object.getDouble("sumRating"));
        degree.setNumVotes(object.getInt("numVotes"));
        degree.setFaculty(object.getString("faculty"));
        degree.setUniversityName(object.getString("universityName"));
        degree.setUniversityID(object.getString("universityID"));

        Type listType = new TypeToken<Location>() {}.getType();
        Location location = new Gson().fromJson(object.getString("location"), listType);

        degree.setLocation(location);

        degrees.add(degree);

        Log.d("DEGREE", "ADDED");

    }

    private void addProfessor(JSONObject object) throws JSONException {
        Professor professor = new Professor();
        professor.setName(object.getString("name"));
        professor.setElo(object.getInt("elo"));
        professor.setTotalSkillRating1(object.getInt("totalSkillRating1"));
        professor.setTotalSkillRating2(object.getInt("totalSkillRating2"));
        professor.setTotalSkillRating3(object.getInt("totalSkillRating3"));
        professor.setTotalSkillRating4(object.getInt("totalSkillRating4"));
        professor.setTotalSkillRating5(object.getInt("totalSkillRating5"));
        professor.setNumVotes(object.getInt("numVotes"));
        professor.setWebUrl(object.getString("webUrl"));
        professor.setUniversityID(object.getString("universityID"));

        Type listType = new TypeToken<List<String>>() {}.getType();
        List<String> degreeIds = new Gson().fromJson(object.getString("degreeIDs"), listType);

        professor.setDegreeIDs(degreeIds);

        professors.add(professor);

        Log.d("PROFESSOR", "ADDED");

    }


    private void sortArraysByElo(){
        Collections.sort(universities, new Comparator<University>() {
            @Override
            public int compare(University lhs, University rhs) {
                return (int) (lhs.getElo() - rhs.getElo());
            }
        });

        Collections.sort(degrees, new Comparator<Degree>() {
            @Override
            public int compare(Degree lhs, Degree rhs) {
                return (int) (lhs.getElo() - rhs.getElo());
            }
        });

        Collections.sort(professors, new Comparator<Professor>() {
            @Override
            public int compare(Professor lhs, Professor rhs) {
                return lhs.getElo() - rhs.getElo();
            }
        });
    }

    public ArrayList<Professor> getProfessors() {
        return professors;
    }

    public ArrayList<Degree> getDegrees() {
        return degrees;
    }

    public ArrayList<University> getUniversities() {
        return universities;
    }

    public ArrayList<String> getProfessorsKeys() {
        return professorsKeys;
    }

    public ArrayList<String> getDegreesKeys() {
        return degreesKeys;
    }

    public ArrayList<String> getUniversitiesKeys() {
        return universitiesKeys;
    }
}
