package com.android.renly.edu_yunzhi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.renly.edu_yunzhi.Activity.EditNoteActivity;
import com.android.renly.edu_yunzhi.Bean.Note;
import com.android.renly.edu_yunzhi.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteAdapter extends BaseAdapter {
    private Context mContext;
    private List<Note> list;

    public NoteAdapter(Context mContext, List<Note> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_note, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final ViewHolder finalHolder = holder;
        holder.ivItemNoteStar.setImageResource(list.get(position).isStar() ? R.drawable.star_fill_note : R.drawable.star_note);
        holder.ivItemNoteStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).isStar()){
                    //已点星
                    list.get(position).setStar(false);
                    finalHolder.ivItemNoteStar.setImageResource(R.drawable.star_note);
                }else{
                    list.get(position).setStar(true);
                    finalHolder.ivItemNoteStar.setImageResource(R.drawable.star_fill_note);
                }
            }
        });
        holder.tvItemNoteContent.setText(list.get(position).getContent());
        holder.tvItemNoteTime.setText(list.get(position).getTime());
        holder.ivItemNoteDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
            }
        });
        holder.itemNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditNoteActivity.class);
                intent.putExtra("Note", JSON.toJSONString(list.get(position)));
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.iv_item_note_star)
        ImageView ivItemNoteStar;
        @BindView(R.id.tv_item_note_content)
        TextView tvItemNoteContent;
        @BindView(R.id.tv_item_note_time)
        TextView tvItemNoteTime;
        @BindView(R.id.iv_item_note_delete)
        ImageView ivItemNoteDelete;
        @BindView(R.id.item_note)
        AbsoluteLayout itemNote;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
