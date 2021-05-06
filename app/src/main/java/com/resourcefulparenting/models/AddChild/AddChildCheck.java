package com.resourcefulparenting.models.AddChild;

import com.resourcefulparenting.models.Query;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class AddChildCheck {
    public String login_token;
    public String child_birth_date;
    public String child_birth_month;
    public String child_birth_year;
    public String child_name;
    public String child_gender;
    public ArrayList<HashMap<String, String>> queries;
}
