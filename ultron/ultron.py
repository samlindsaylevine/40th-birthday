from tkinter import *
from tkinter import ttk

root = Tk()

# Hide title bar entirely; icon and title in case of alt-tab
root.title("Ultron")
root.iconbitmap("ultron-face.ico")
root.overrideredirect(True)

frame = ttk.Frame(root)
frame.grid(column=1, row=3, sticky=(W, E), padx=50, pady=50)

ttk.Label(frame,
          text="These pathetic creatures are trying to infiltrate my mind. They will never be able to determine my "
               "shutdown code.\n\n"
               "They likely have not even penetrated far enough to read my surface thoughts. Even if they had,"
               "they will never be able to figure out how to operate my Ultron drone controls, located under the couch.",
          wraplength="600",
          font=("Arial", 24)
          ).grid(column=1, row=1)

entryBox = ttk.Entry(frame)
entryBox.grid(column=1, row=2, pady=20)


def shutdown(*args):
    text = entryBox.get()
    new_text = ("I cannot be shut down without the code." if text == "" else
                "The correct answer" if text == "correct" else
                "They fumble in error! " + text + " is not the correct code.")
    resultText['text'] = new_text


ttk.Button(frame, text="Shutdown", command=shutdown).grid(column=1, row=3)

resultText = ttk.Label(frame, text="", wraplength="600", font=("Arial", 24))
resultText.grid(column=1, row=4)

root.eval('tk::PlaceWindow . center')

root.mainloop()
