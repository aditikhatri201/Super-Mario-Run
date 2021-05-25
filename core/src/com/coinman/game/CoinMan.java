package com.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[]man;
	int manState=0;
	int pause=0;
	float gravity=0.2f;
	float velocity=0;
	int manY=0;
	Rectangle manRectangle;
	BitmapFont font;

	ArrayList<Integer> coinX=new ArrayList<Integer>();
	ArrayList<Integer> coinY=new ArrayList<Integer>();
	ArrayList<Rectangle> coinRect=new ArrayList<Rectangle>();
	Texture coin;
	int coinCount=0;
	int score=0;
	int gameState=0;

	ArrayList<Integer> bombX=new ArrayList<Integer>();
	ArrayList<Integer> bombY=new ArrayList<Integer>();
	ArrayList<Rectangle> bombRect=new ArrayList<Rectangle>();
	Texture bomb;
	int bombCount=0;
	Texture dizzy;

	Random random;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");
		man=new Texture[4];
		man[0]=new Texture("frame-1.png");
		man[1]=new Texture("frame-2.png");
		man[2]=new Texture("frame-3.png");
		man[3]=new Texture("frame-4.png");
		manY=Gdx.graphics.getHeight()/2 ;
		coin=new Texture("coin.png");
		bomb=new Texture("bomb.png");
		random=new Random();
		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		dizzy=new Texture("dizzy-1.png");


	}
	public  void makeCoin(){
		float height=random.nextFloat()*Gdx.graphics.getHeight();
		coinY.add((int)height);
		coinX.add(Gdx.graphics.getWidth());

	}
	public  void makeBomb(){
		float height=random.nextFloat()*Gdx.graphics.getHeight();
		bombY.add((int)height);
		bombX.add(Gdx.graphics.getWidth());

	}


	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if (gameState == 1) {
			//Game is live
			//coin
			if (coinCount < 100) {
				coinCount++;
			} else {
				coinCount = 0;
				makeCoin();
			}
			coinRect.clear();
			for (int i = 0; i < coinX.size(); i++) {
				batch.draw(coin, coinX.get(i), coinY.get(i));
				coinX.set(i, coinX.get(i) - 4);
				coinRect.add(new Rectangle(coinX.get(i), coinY.get(i), coin.getWidth(), coin.getHeight()));
			}
			//bomb
			if (bombCount < 250) {
				bombCount++;
			} else {
				bombCount = 0;
				makeBomb();
			}
			bombRect.clear();
			for (int i = 0; i < bombX.size(); i++) {
				batch.draw(bomb, bombX.get(i), bombY.get(i));
				bombX.set(i, bombX.get(i) - 8);
				bombRect.add(new Rectangle(bombX.get(i), bombY.get(i), bomb.getWidth(), bomb.getHeight()));

			}


			if (Gdx.input.justTouched()) {
				velocity = -10;
			}
			if (pause < 8) {
				pause++;
			} else {
				pause = 0;
				if (manState < 3) {
					manState++;
				} else {
					manState = 0;
				}
			}
			velocity += gravity;
			manY -= velocity;
			if (manY <= 0) {
				manY = 0;
			}
		} else if (gameState == 0) {
			//waiting to start
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		} else if (gameState == 2) {
			//game over
			if (Gdx.input.justTouched()) {
				gameState = 1;
				score = 0;
				manY=Gdx.graphics.getHeight()/2 ;
				velocity=0;
				coinX.clear();
				coinY.clear();
				coinRect.clear();
				bombCount=0;
				bombX.clear();
				bombY.clear();
				bombRect.clear();
				bombCount=0;
			}
		}
		if(gameState==2){
			batch.draw(dizzy,((Gdx.graphics.getWidth() / 2) - man[manState].getWidth() / 2), manY);
		}else{
			batch.draw(man[manState], ((Gdx.graphics.getWidth() / 2) - man[manState].getWidth() / 2), manY);
		}


			manRectangle = new Rectangle((Gdx.graphics.getWidth() / 2) - man[manState].getWidth() / 2, manY, man[manState].getWidth(), man[manState].getWidth());
			for (int i = 0; i < coinRect.size(); i++) {
				if (Intersector.overlaps(manRectangle, coinRect.get(i))) {
					score++;
					coinRect.remove(i);
					coinX.remove(i);
					coinY.remove(i);
					break;
				}
			}
			for (int i = 0; i < bombRect.size(); i++) {
				if (Intersector.overlaps(manRectangle, bombRect.get(i))) {
					gameState = 2;

				}
			}
			font.draw(batch, String.valueOf(score), 100, 200);
			batch.end();
		}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
