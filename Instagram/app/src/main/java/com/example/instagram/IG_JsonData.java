package com.example.instagram;

import android.util.Log;

import com.example.instagram.model.Edges;
import com.example.instagram.model.Node;
import com.example.instagram.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IG_JsonData {

    public static ArrayList<User> getUserJsonData(String data){
        ArrayList<User> userList = new ArrayList<>();
        try {
            User user = new User();
            JSONObject jsonObject = new JSONObject(data).getJSONObject("graphql").getJSONObject("user");
            user.setBiography(jsonObject.getString("biography"));
            user.setExternal_url(jsonObject.getString("external_url"));
            user.getEdge_followed_by().setCount(jsonObject.getJSONObject("edge_followed_by").getInt("count"));
            user.getEdge_follow().setCount(jsonObject.getJSONObject("edge_follow").getInt("count"));
            user.setFull_name(jsonObject.getString("full_name"));
            user.setProfile_pic_url_hd(jsonObject.getString("profile_pic_url_hd"));
            user.setUsername(jsonObject.getString("username"));
            //貼文(Grid)
            JSONObject jsonObject_grid = jsonObject.getJSONObject("edge_owner_to_timeline_media");
            user.getEdge_owner_to_timeline_media().setCount(jsonObject_grid.getInt("count"));
            Edges edges = new Edges();
            for (int i=0; i<jsonObject_grid.getJSONArray("edges").length(); i++){
                JSONObject jsonObject_node = jsonObject_grid.getJSONArray("edges").getJSONObject(i).getJSONObject("node");
                Node node = new Node();
                node.setShortcode(jsonObject_node.getString("shortcode"));
                //dimensions
                node.setDisplay_url(jsonObject_node.getString("display_url"));
                //isVideo
                if (jsonObject_node.getJSONObject("edge_media_to_caption").getJSONArray("edges").length() > 0)
                    node.getEdge_media_to_caption().getEdges_inner().getNode_inner().setText(jsonObject_node.getJSONObject("edge_media_to_caption").getJSONArray("edges").getJSONObject(0).getJSONObject("node").getString("text"));
                node.getEdge_media_to_comment().setCount(jsonObject_node.getJSONObject("edge_media_to_comment").getInt("count"));
                node.setTaken_at_timestamp(jsonObject_node.getInt("taken_at_timestamp"));
                node.getEdge_liked_by().setCount(jsonObject_node.getJSONObject("edge_liked_by").getInt("count"));
                //edge_media_preview_like
                //location
                Edges edges_child = new Edges(); //edge_sidecar_to_children
                if(jsonObject_node.has("edge_sidecar_to_children")){
                    for (int j=0; j<jsonObject_node.getJSONObject("edge_sidecar_to_children").getJSONArray("edges").length(); j++){
                        JSONObject jsonObject_node_child = jsonObject_node.getJSONObject("edge_sidecar_to_children").getJSONArray("edges").getJSONObject(j).getJSONObject("node");
                        Node node_child = new Node();
                        node_child.setShortcode(jsonObject_node_child.getString("shortcode"));
                        node_child.setDisplay_url(jsonObject_node_child.getString("display_url"));
                        edges_child.getNodeList().add(node_child);
                    }
                }else {
                    edges_child.getNodeList().add(node);
                }
                //video_duration
                node.setEdge_sidecar_to_children(edges_child);
                edges.getNodeList().add(node);
            }
            user.getEdge_owner_to_timeline_media().setEdges(edges);
            userList.add(user);
        } catch (JSONException e) {
            Log.e("IG_JsonData", "getUserJsonData-" + e.getMessage());
        }
        return userList;
    }
}
