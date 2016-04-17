package com.rahulsah.diary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiaryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiaryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ArrayAdapter<String> adapter;
    ListView notesList;
    ArrayList<Note> notes_list = new ArrayList<Note>();
    TextView noNotesFound;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FloatingActionButton newNoteButton;

    private OnFragmentInteractionListener mListener;

    public DiaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiaryFragment newInstance(String param1, String param2) {
        DiaryFragment fragment = new DiaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_diary, container, false);

        noNotesFound = (TextView) v.findViewById(R.id.no_notes_found);

        refresh();

        if(notes_list.size() > 0) {
            noNotesFound.setVisibility(View.GONE);
        }

        notesList = (ListView) v.findViewById(R.id.note_list_view);
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_2, android.R.id.text1, notes_list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(notes_list.get(position).getTitle());
                text2.setText(first10Words(notes_list.get(position).getEntry()));
                return view;
            }

            @Override
            public int getCount() {
                return notes_list.size();
            }
        };

        notesList.setAdapter(adapter);

        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent2 = new Intent(getActivity(), ShowNoteActivity.class);

                Bundle bundle = new Bundle();
                bundle.putLong(ShowNoteActivity.EntryId, notes_list.get(i).getid());

                intent2.putExtras(bundle);
                startActivity(intent2);
            }
        });

        notesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int elementId, long l) {
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Note note = Note.findById(Note.class, notes_list.get(elementId).getId());

                                notes_list.remove(elementId);
                                note.delete();

                                adapter.notifyDataSetChanged();
                                notesList.setAdapter(adapter);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });

        newNoteButton = (FloatingActionButton) v.findViewById(R.id.new_note_button);
        newNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newNoteIntent = new Intent(getActivity(), NewNoteActivity.class);

                newNoteIntent.putExtra(NewNoteActivity.Mode, NewNoteActivity.NewMode);
                startActivity(newNoteIntent);
            }
        });
        return v;
    }

    static String first10Words(String s) {
        List<String> l = new ArrayList<String>();
        int pos = 0;
        while (l.size() < 12) {
            int newPos = s.indexOf(' ', pos);
            if (newPos == -1) {
                l.add(s.substring(pos));
                break;
            }
            l.add(s.substring(pos, newPos));
            pos = newPos + 1;
        }

        String[] words = l.toArray(new String[0]);
        String finalWord = "";

        for (String word: words) {
            finalWord += " " + word;
        }

        return finalWord;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        notes_list = new ArrayList<>();
        Iterator<Note> notes = Note.findAll(Note.class);

        while(notes.hasNext()) {
            notes_list.add(notes.next());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
