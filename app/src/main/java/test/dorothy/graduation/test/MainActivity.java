package test.dorothy.graduation.test;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;

import java.util.List;


public class MainActivity extends Activity {
    String bookXml = "<book><title>Pride and Prejudice</title>\n" +
            "<author>Jane Austen</author>\n" +
            "<chapter>chapter1</chapter>\n" +
            "<content><p>It is a truth universally acknowledged, that a single man in possession of a good fortune, must be in want of a wife.\n" +
            "However little known the feelings or views of such a man may be on his first entering a neighbourhood, this truth is so well fixed in the minds of the surrounding families, that he is considered the rightful property of some one or other of their daughters.</p>\n" +
            "<p>\"My dear Mr. Bennet,\" said his lady to him one day, \"have you heard that Netherfield Park is let at last?\"</p>\n" +
            "<p>Mr. Bennet replied that he had not.</p>\n" +
            "<p>\"But it is,\" returned she; \"for Mrs. Long has just been here, and she told me all about it.\"</p>\n" +
            "<p>Mr. Bennet made no answer.</p>\n" +
            "<p>\"Do you not want to know who has taken it?\" cried his wife impatiently.</p>\n" +
            "<p>\"You want to tell me, and I have no objection to hearing it.\"</p>\n" +
            "<p>This was invitation enough.</p>\n" +
            "<p>\"Why, my dear, you must know, Mrs. Long says that Netherfield is taken by a young man of large fortune from the north of England; that he came down on Monday in a chaise and four to see the place, and was so much delighted with it, that he agreed with Mr. Morris immediately; that he is to take possession before Michaelmas, and some of his servants are to be in the house by the end of next week.\"</p>\n" +
            "<p>\"What is his name?\"</p>\n" +
            "<p>\"Bingley.\"</p>\n" +
            "<p>\"Is he married or single?\"</p>\n" +
            "<p>\"Oh! Single, my dear, to be sure! A single man of large fortune; four or five thousand a year. What a fine thing for our girls!\"</p>\n" +
            "<p>\"How so? How can it affect them?\"</p>\n" +
            "<p>\"My dear Mr. Bennet,\" replied his wife, \"how can you be so tiresome! You must know that I am thinking of his marrying one of them.\"</p>\n" +
            "<p>\"Is that his design in settling here?\"</p>\n" +
            "<p>\"Design! Nonsense, how can you talk so! But it is very likely that he may fall in love with one of them, and therefore you must visit him as soon as he comes.\"</p>\n" +
            "<p>\"I see no occasion for that. You and the girls may go, or you may send them by themselves, which perhaps will be still better, for as you are as handsome as any of them, Mr. Bingley may like you the best of the party.\"</p>\n" +
            "<p>\"My dear, you flatter me. I certainly have had my share of beauty, but I do not pretend to be anything extraordinary now. When a woman has five grown-up daughters, she ought to give over thinking of her own beauty.\"</p>\n" +
            "<p>\"In such cases, a woman has not often much beauty to think of.\"</p>\n" +
            "<p>\"But, my dear, you must indeed go and see Mr. Bingley when he comes into the neighbourhood.\"</p>\n" +
            "<p>\"It is more than I engage for, I assure you.\"</p>\n" +
            "<p>\"But consider your daughters. Only think what an establishment it would be for one of them. Sir William and Lady Lucas are determined to go, merely on that account, for in general, you know, they visit no newcomers. Indeed you must go, for it will be impossible for us to visit him if you do not.\"</p>\n" +
            "<p>\"You are over-scrupulous, surely. I dare say Mr. Bingley will be very glad to see you; and I will send a few lines by you to assure him of my hearty consent to his marrying whichever he chooses of the girls; though I must throw in a good word for my little Lizzy.\"</p>\n" +
            "<p>\"I desire you will do no such thing. Lizzy is not a bit better than the others; and I am sure she is not half so handsome as Jane, nor half so good-humoured as Lydia. But you are always giving her the preference.\"</p>\n" +
            "<p>\"They have none of them much to recommend them,\" replied he; \"they are all silly and ignorant like other girls; but Lizzy has something more of quickness than her sisters.\"</p>\n" +
            "<p>\"Mr. Bennet, how can you abuse your own children in such a way? You take delight in vexing me. You have no compassion for my poor nerves.\"</p>\n" +
            "<p>\"You mistake me, my dear. I have a high respect for your nerves. They are my old friends. I have heard you mention them with consideration these last twenty years at least.\"</p>\n" +
            "<p>\"Ah, you do not know what I suffer.\"</p>\n" +
            "<p>\"But I hope you will get over it, and live to see many young men of four thousand a year come into the neighbourhood.\"</p>\n" +
            "<p>\"It will be no use to us, if twenty such should come, since you will not visit them.\"</p>\n" +
            "<p>\"Depend upon it, my dear, that when there are twenty, I will visit them all.\"</p>\n" +
            "<p>Mr. Bennet was so odd a mixture of quick parts, sarcastic humour, reserve, and caprice, that the experience of three-and-twenty years had been insufficient to make his wife understand his character. Her mind was less difficult to develop. She was a woman of mean understanding, little information, and uncertain temper. When she was discontented, she fancied herself nervous. The business of her life was to get her daughters married; its solace was visiting and news.</p></content></book>";
    private Book book;
    private PageView pageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        book = new BookXmlParser().parser(bookXml);
        pageView = (PageView) findViewById(R.id.page);
        PaintInfo paintInfo = setPaintInfo();
        List<PageSpan> pageSpanList = new BookSpanGenerator().setupPage(book,paintInfo);
        pageView.setPageSpan(pageSpanList.get(0));
        pageView.setPaintInfo(paintInfo);

    }


    private PaintInfo setPaintInfo(){
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        PaintInfo paintInfo = new PaintInfo();
        paintInfo.setScreenW(width);
        paintInfo.setScreenH(height);
        paintInfo.setSpaceH((int)getResources().getDimension(R.dimen.height10));
        paintInfo.setSpaceW((int)getResources().getDimension(R.dimen.width5));
        paintInfo.setPaddingTop((int)getResources().getDimension(R.dimen.padding10));
        paintInfo.setPaddingBottom((int)getResources().getDimension(R.dimen.padding10));
        paintInfo.setPaddingLeft((int)getResources().getDimension(R.dimen.padding10));
        paintInfo.setPaddingRight((int)getResources().getDimension(R.dimen.padding10));

        Paint paint = new Paint();
        paint.setTextSize(getResources().getDimension(R.dimen.textsize16));
        paint.setColor(Color.BLACK);
        paintInfo.setPaint(paint);

        return paintInfo;
    }



}
