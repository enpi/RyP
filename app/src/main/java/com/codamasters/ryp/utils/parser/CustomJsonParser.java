package com.codamasters.ryp.utils.parser;

import android.util.Log;

import com.codamasters.ryp.model.Degree;
import com.codamasters.ryp.model.Professor;
import com.codamasters.ryp.model.University;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Juan on 20/08/2016.
 */
public class CustomJsonParser {

    private JSONObject jsonObject;
    private ArrayList<Professor> professors;
    private ArrayList<Degree> degrees;
    private ArrayList<University> universities;

    public CustomJsonParser(JSONObject jsonObject){
        this.jsonObject = jsonObject;

        universities = new ArrayList<>();
        degrees = new ArrayList<>();
        professors = new ArrayList<>();
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
                    addUniversity(content);
                    break;
                case "degree":
                    addDegree(content);
                    break;
                case "professor":
                    addProfessor(content);
                    break;
            }
        }

        sortArraysByElo();
    }

    private void addUniversity(JSONObject object) throws JSONException {
        University university = new University();
        university.setName(object.getString("name"));
        university.setSumElo(object.getDouble("sumElo"));
        university.setSumRating(object.getDouble("sumRating"));
        university.setNumVotes(object.getInt("numVotes"));

        universities.add(university);

        Log.d("UNIVERSITY", "ADDED");
    }

    private void addDegree(JSONObject object) throws JSONException {
        Degree degree = new Degree();
        degree.setName(object.getString("name"));
        degree.setSumElo(object.getDouble("sumElo"));
        degree.setSumRating(object.getDouble("sumRating"));
        degree.setNumVotes(object.getInt("numVotes"));

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

        professors.add(professor);

        Log.d("PROFESSOR", "ADDED");

    }


    private void sortArraysByElo(){
        Collections.sort(universities, new Comparator<University>() {
            @Override
            public int compare(University lhs, University rhs) {
                return (int) (lhs.getSumElo() - rhs.getSumElo());
            }
        });

        Collections.sort(degrees, new Comparator<Degree>() {
            @Override
            public int compare(Degree lhs, Degree rhs) {
                return (int) (lhs.getSumElo() - rhs.getSumElo());
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
}
