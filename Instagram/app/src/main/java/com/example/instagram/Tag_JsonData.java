package com.example.instagram;

import android.util.Log;

import com.example.instagram.model.Edges;
import com.example.instagram.model.Node;
import com.example.instagram.model.Tag;

import org.json.JSONException;
import org.json.JSONObject;

public class Tag_JsonData {

    public static Tag getTagData(String data) {
        Tag tag = new Tag();
        try {
            JSONObject jsonObject = new JSONObject(data).getJSONObject("graphql").getJSONObject("hashtag").getJSONObject("edge_hashtag_to_media");
            tag.setCount(jsonObject.getInt("count"));
            Edges edges = new Edges();
            for (int i=0; i<jsonObject.getJSONArray("edges").length(); i++){
                JSONObject jsonObject_node = jsonObject.getJSONArray("edges").getJSONObject(i).getJSONObject("node");
                Node node = new Node();
                node.setShortcode(jsonObject_node.getString("shortcode"));
                node.getDimensions().setHeight(jsonObject_node.getJSONObject("dimensions").getInt("height"));
                node.getDimensions().setWidth(jsonObject_node.getJSONObject("dimensions").getInt("width"));
                node.setDisplay_url(jsonObject_node.getString("display_url"));
                //isVideo
                node.getEdge_media_to_caption().getEdges_inner().getNode_inner().setText(jsonObject_node.getJSONObject("edge_media_to_caption").getJSONArray("edges").getJSONObject(0).getJSONObject("node").getString("text"));
                node.getEdge_media_to_comment().setCount(jsonObject_node.getJSONObject("edge_media_to_comment").getInt("count"));
                node.setTaken_at_timestamp(jsonObject_node.getInt("taken_at_timestamp"));
                node.getEdge_liked_by().setCount(jsonObject_node.getJSONObject("edge_liked_by").getInt("count"));
                //edge_media_preview_like
                edges.getNodeList().add(node);
            }
            tag.setEdges(edges);
        } catch (JSONException e) {
            Log.e("Tag_JsonData", "getTagData-" + e.getMessage());
            return null;
        }
        return tag;
    }
}
