package com.iarpg.app.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.iarpg.app.data.AIIntegration;
import com.iarpg.app.data.Choice;
import com.iarpg.app.data.Item;
import com.iarpg.app.data.PlayerCharacter;
import com.iarpg.app.data.Room;
import com.iarpg.app.data.Utils;
import com.iarpg.app.databinding.FragmentGameBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {
    private FragmentGameBinding binding;

    private String theme;

    private List<String> roomTitles;

    private String characterClass;
    
    private int currentRoomIndex;

    private List<Room> rooms;

    private PlayerCharacter playerCharacter;
    private String waitText;


    public static GameFragment newInstance() {
        GameFragment fragment = new GameFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGameBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        GameFragment self = this;

        ExecutorService service = Executors.newSingleThreadExecutor();

        Handler handler = new Handler(Looper.getMainLooper());

        Bundle bundle = getArguments();
        assert bundle != null;
        this.characterClass = bundle.getString("characterClass");
        this.playerCharacter = new PlayerCharacter(bundle.getString("characterClass"), bundle.getString("characterBackstory"));

        this.theme = bundle.getString("theme");


        waitText = "Les lutins préparent la partie.\nMerci de patienter.";
        binding.textZone.setText(waitText);

        this.updateHpText();

        this.generateRoomTitles(self, service, handler);

        binding.textZone.setMovementMethod(new android.text.method.ScrollingMovementMethod());
        setActionButtonsState(false);

        initActionButton(self, binding.actionButton1, 1);

        initActionButton(self, binding.actionButton2, 2);

        initNextButton(self);


    }

    private void initNextButton(GameFragment self) {
        binding.nextButton.setEnabled(false);
        binding.nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                self.goToNextRoom();
                binding.nextButton.setEnabled(false);
            }
        });
    }

    private void updateHpText() {
        String text = String.format("PV : %d/%d", this.playerCharacter.getHealthPoints(), this.playerCharacter.getMaxHealthPoints());
        binding.hp.setText(text);
    }

    private void goToNextRoom() {
        this.currentRoomIndex++;

        System.out.println("HP Restant : " + this.playerCharacter.getHealthPoints());

        if (this.playerCharacter.getHealthPoints() <= 0)  { // Player à court d'HP
            this.lose();
        }

        else if (this.currentRoomIndex >= 10) {
            // Victoire
            this.win();
        }
        else {
            this.gameTurn();
        }



    }

    private void win() {
        this.setActionButtonsState(false);
        binding.nextButton.setEnabled(false);

        FragmentManager fragmentManager = getParentFragmentManager();

        WinFragment winFragment = WinFragment.newInstance();
        Utils.fragmentTransition(fragmentManager, winFragment, false);

    }

    private void lose() {
        this.setActionButtonsState(false);
        binding.nextButton.setEnabled(false);

        FragmentManager fragmentManager = getParentFragmentManager();

        LoseFragment loseFragment = LoseFragment.newInstance();
        Utils.fragmentTransition(fragmentManager, loseFragment, false);

    }

    private void initActionButton(GameFragment self, Button button, int actionNumber) {
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                self.displayConsequence(actionNumber);
                self.setActionButtonsState(false);
            }
        });
    }

    private void generateRoomTitles(GameFragment self, ExecutorService service, Handler handler) {
        service.execute(new Runnable() {

            @Override
            public void run() {
                List<String> roomTitles = new ArrayList<>();

                try {
                    roomTitles = AIIntegration.generateRoomTitles(self.theme);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                List<String> finalRoomTitles = roomTitles;
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (finalRoomTitles != null) {
                            //binding.textZone.setText(finalRoomTitles.toString());
                            self.roomTitles = finalRoomTitles;

                            self.initRoomList(self, service, handler);
                        }
                    }
                });
            }
        });
    }

    private void initRoomList(GameFragment self, ExecutorService service, Handler handler) {

        service.execute(new Runnable() {

            @Override
            public void run() {

                List<Room> rooms = new ArrayList<>();
                String previousRoomTitle;
                for (int i=0; i < 10; i++) {

                    String textString = waitText + String.format(" (%d/10)", i + 1);
                    binding.textZone.setText(textString);

                    String roomTitle = self.roomTitles.get(i);
                    Map<String, String> result;

                    previousRoomTitle = i == 0 ? "Auberge" : rooms.get(i - 1).getTitle();
                    int hp = i == 0 ? self.playerCharacter.getMaxHealthPoints() : rooms.get(i -1).getNextHealthPoints();

                    try {
                        result = AIIntegration.generateRoomDescription(roomTitle, previousRoomTitle, false, self.characterClass, hp);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    String roomDescription = result.get("roomDescription");
                    Choice choice = new Choice(result.get("choice1"), result.get("choice2"), result.get("consequence1"), result.get("consequence2"), result.get("correctChoice"));
                    Item item = new Item(result.get("itemName"), result.get("itemDescription"));
                    int nextHealthPoints = Integer.parseInt(result.get("nextHealthPoints"));


                    Room room = new Room(roomTitle, roomDescription, choice, item, nextHealthPoints);

                    rooms.add(room);
                }

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        self.rooms = rooms;
                        self.currentRoomIndex = 0;
                        self.gameTurn();
                    }
                });
            }
        });
    }

    private void gameTurn() {
        binding.textZone.setText(this.rooms.get(this.currentRoomIndex).getGameString());
        
        this.setActionButtonsState(true);
    }
    
    private void displayConsequence(int actionNumber) {
        String stringToDisplay = null; 
        switch(actionNumber) {
            case 1:
                stringToDisplay = this.rooms.get(this.currentRoomIndex).getChoice().getConsequence1();
                break;
            case 2:
                stringToDisplay = this.rooms.get(this.currentRoomIndex).getChoice().getConsequence2();
                break;
        }

        String correctChoice = this.rooms.get(currentRoomIndex).getChoice().getCorrectChoice();

        if (! correctChoice.equals(String.valueOf(actionNumber))) {
            this.playerCharacter.setHealthPoints(this.rooms.get(currentRoomIndex).getNextHealthPoints());
            this.updateHpText();
        }


        binding.textZone.setText(stringToDisplay);
        binding.textZone.scrollTo(0, 0);

        binding.nextButton.setEnabled(true);
    }


    private void setActionButtonsState(boolean state) {
        binding.actionButton1.setEnabled(state);
        binding.actionButton2.setEnabled(state);
    }
}