package com.example.dell.smartpos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class MenuIconAdapter extends RecyclerView.Adapter<MenuViewHolder> {

    private ArrayList<MenuIcon> iconList;
    private Context context;
    private MenuIconAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MenuIcon item);
    }

    public MenuIconAdapter(ArrayList<MenuIcon> list, Context context, MenuIconAdapter.OnItemClickListener listener) {
        this.iconList = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.mainmenu_item, parent, false);

        MenuViewHolder viewHolder = new MenuViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {

        holder.menuName.setText(iconList.get(position).getMenuName());
        holder.menuIcon.setImageResource(iconList.get(position).getMenuIcon());

        holder.menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(iconList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return iconList.size();
    }
}

class MenuViewHolder extends RecyclerView.ViewHolder {

    RelativeLayout menuLayout;
    ImageView menuIcon;
    TextView menuName;

    public MenuViewHolder(View view) {
        super(view);

        menuLayout = itemView.findViewById(R.id.menuLayout);
        menuName = itemView.findViewById(R.id.iconName);
        menuIcon = itemView.findViewById(R.id.icon);
    }
}

class MenuIcon implements Serializable {

    int menuIcon;
    String menuName;

    public MenuIcon(int icon, String name) {

        super();
        this.menuIcon = icon;
        this.menuName = name;
    }

    public int getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(int menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

}
